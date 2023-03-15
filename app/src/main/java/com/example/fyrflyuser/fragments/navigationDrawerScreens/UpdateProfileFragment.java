package com.example.fyrflyuser.fragments.navigationDrawerScreens;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.FragmentUpdateProfileBinding;
import com.example.fyrflyuser.modelClasses.ProfileRoot;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class UpdateProfileFragment extends Fragment {

    FragmentUpdateProfileBinding binding;
    String firstName, lastName, email, phone, stringPhotoPath;
    static int PICK_IMAGE_PHOTO = 1;
    public static Uri imageUri;
    int selectId ;
    private SegmentedButtonGroup mRadioGroup;

    String accountType;


    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        setOnClick();

        return binding.getRoot();
    }

    private void setOnClick() {

        binding.update.setOnClickListener(v -> {

            apiUpdateProfile();
        });

        binding.userImage.setOnClickListener(v -> {
//
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            startActivityForResult(intent, PICK_IMAGE_PHOTO);

            ImagePicker.Companion.with(UpdateProfileFragment.this).crop().compress(512).maxResultSize(1080, 1080).start();

        });

        binding.radioRealButtonGroup.setPosition(0, false);


    }

    private void apiUpdateProfile() {


        firstName = binding.firstName.getText().toString().trim();
        lastName = binding.lastName.getText().toString().trim();
        email = binding.EditEmail.getText().toString().trim();
        phone = binding.EditPhone.getText().toString().trim();

        if (firstName.isEmpty()) {
            binding.firstName.setError("Enter Username");
            binding.firstName.requestFocus();
        } else if (lastName.isEmpty()) {
            binding.lastName.setError("Enter Last Name");
            binding.lastName.requestFocus();
        } else if (email.isEmpty()) {
            binding.EditEmail.setError("Enter Email");
            binding.EditEmail.requestFocus();
        } else if (phone.isEmpty()) {
            binding.EditPhone.setError("Enter Phone");
            binding.EditPhone.requestFocus();
        }
        else {

            Toast.makeText(requireContext(), "photo"+stringPhotoPath, Toast.LENGTH_SHORT).show();
            new Mvvm().editProfile(requireActivity(),
                    App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN),
                    CommonUtils.getRequestBodyText(CommonUtils.getUserId()),
                    CommonUtils.getRequestBodyText(firstName),
                    CommonUtils.getRequestBodyText(lastName),
                    CommonUtils.getRequestBodyText(email),
                    CommonUtils.getRequestBodyText(phone),
                    CommonUtils.getFileData(stringPhotoPath, "image")).observe(requireActivity(),
                    new Observer<ProfileRoot>() {
                        @Override
                        public void onChanged(ProfileRoot profileRoot) {
                            if (profileRoot != null) {

                                if (profileRoot.getStatus() == 1) {


                                    //  App.getPrefManager().saveModel("RootClass", profileRoot.getDetails());
                                    Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new ProfileFragment()).addToBackStack(null).commit();
                                } else {
                                    Toast.makeText(requireContext(), "Update Profile Fail---"+profileRoot.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                            }
                        }
                    });
            selectId = binding.radioRealButtonGroup.getPosition();

            if (selectId == 1) {
                accountType = "Drivers";
            } else {
                accountType = "Customers";
            }

            String name = firstName+" "+lastName ;
            String user_id = CommonUtils.getUserId() ;
            Map<String, Object> newUserMap = new HashMap<>();
            newUserMap.put("name", name);
            newUserMap.put("profileImageUrl", "default");
            newUserMap.put("UniqueId", App.getPrefManager().getPreferences(AppConstants.UNIQUE_ID));
            if (accountType.equals("Drivers")) {
                newUserMap.put("service", "type_1");
                newUserMap.put("activated", true);
                newUserMap.put("UniqueId", App.getPrefManager().getPreferences(AppConstants.UNIQUE_ID));

            }
            FirebaseDatabase.getInstance().getReference().child("Users").child(accountType).child(user_id).updateChildren(newUserMap).addOnCompleteListener((OnCompleteListener<Void>) task -> {
                Toast.makeText(requireContext(), "Update ", Toast.LENGTH_SHORT).show();

            });

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            stringPhotoPath = imageUri.getPath();
            binding.userImage.setImageURI(imageUri);
        } else {
            Toast.makeText(getContext(), "can't get image", Toast.LENGTH_SHORT).show();
        }
    }

    private String uriToStringConvert(Uri newUri) {
        String path;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(newUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        path = cursor.getString(columnIndex);
        cursor.close();
        Toast.makeText(requireActivity(), "" + path, Toast.LENGTH_SHORT).show();
        return path;
    }
}