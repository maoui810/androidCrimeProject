package android.bignerdranch.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.util.UUID;

public class PhotoFragment extends DialogFragment {

    private File mCrimePhotoFile;
    private ImageView mCrimePhoto;
    private int mLastCrimePhotoHeight = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getArguments().getSerializable(CRIME_ID);
        Crime crime = CrimeLab.get(getActivity()).getCrime(uuid);

        mCrimePhotoFile = CrimeLab.get(getActivity()).getPhotoFile(crime);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
        mCrimePhoto = (ImageView) view.findViewById(R.id.crime_photo);

        ViewTreeObserver viewTreeObserver = mCrimePhoto.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updatePhoto();
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(view).create();

    }

    private void updatePhoto() {
        int width = mCrimePhoto.getWidth();
        int height = mCrimePhoto.getHeight();
        if(height != mLastCrimePhotoHeight){
            mLastCrimePhotoHeight = height;
            Bitmap bitmap = PictureUtils.getScaledBitmap(mCrimePhotoFile.getAbsolutePath(), width, height);
            mCrimePhoto.setImageBitmap(bitmap);
        }
    }

    private static final String CRIME_ID = "CRIME_ID";
    public  static PhotoFragment newInstance(UUID uuid){
        Bundle bundle = new Bundle();
        bundle.putSerializable(CRIME_ID, uuid);
        PhotoFragment photoFragment = new PhotoFragment();
        photoFragment.setArguments(bundle);
        return photoFragment;
    }
}

