package com.example.movieapp.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.movieapp.R;
import com.example.movieapp.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private Context context;
    Button selectImage, saveImage, savePassword;
    ImageView imageView;
    DatabaseHelper db;
    EditText psd;
    public static final String SHARED_PREFS = "sharedPrefs";
    private int REQUEST_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        selectImage = root.findViewById(R.id.selectButton);
        saveImage = root.findViewById(R.id.saveButton);
        savePassword = root.findViewById(R.id.button2);
        imageView = root.findViewById(R.id.imageView2);
        psd = root.findViewById(R.id.newpassEdit);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("username", "");
        db = new DatabaseHelper(context);
        Bitmap picture = db.getImage(user);
        if (picture == null) {
            imageView.setImageResource(R.drawable.baseline_star_black_18dp);
        } else {
            imageView.setImageBitmap(picture);
        }

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String path = pictureDirectory.getPath();
                Uri data = Uri.parse(path);
                intent.setDataAndType(data, "image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DatabaseHelper(context);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] data = baos.toByteArray();
                SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("username", "");
                db.addImage(user, data);
                Toast.makeText(getActivity(), "New Image Is Added!", Toast.LENGTH_SHORT).show();
            }
        });
        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DatabaseHelper(context);
                String password = psd.getText().toString();
                SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("username", "");
                db.changePassword(user, password);
                psd.setText("");
                Toast.makeText(getActivity(), "New Password Is Added!", Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                Uri uri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(uri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}