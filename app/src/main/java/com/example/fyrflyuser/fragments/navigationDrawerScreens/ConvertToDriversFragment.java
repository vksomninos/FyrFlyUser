package com.example.fyrflyuser.fragments.navigationDrawerScreens;

import static android.app.Activity.RESULT_OK;
import static com.example.fyrflyuser.fragments.navigationDrawerScreens.UpdateProfileFragment.PICK_IMAGE_PHOTO;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyrflyuser.R;
import com.example.fyrflyuser.activites.HomeDriverActivity;
import com.example.fyrflyuser.databinding.FragmentConvertToDriversBinding;
import com.example.fyrflyuser.modelClasses.CarTypeRoot;
import com.example.fyrflyuser.modelClasses.ConvertDriverRoot;
import com.example.fyrflyuser.modelClasses.ProfileRoot;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.List;


public class ConvertToDriversFragment extends Fragment {
    FragmentConvertToDriversBinding binding;
    String selectedCar = "", brandNameCar, vehicleModel, vehicleReg, driverInfo, stringPhotoPath =""  ,cityinfo;
    private List<String> carTypeList = new ArrayList<String>();
    static int PICK_IMAGE_PHOTO = 1;
    public static Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConvertToDriversBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onclick();
        getCarTypes();
        setCarTypeSpinner();

    }

    private void apiApplyForDriver() {
        brandNameCar = binding.brandName.getText().toString();
        vehicleModel = binding.modelNo.getText().toString();
        vehicleReg = binding.VehicleReg.getText().toString();
        driverInfo = binding.DriverInfo.getText().toString();
        cityinfo = binding.cityinfo.getText().toString();

        if (brandNameCar.isEmpty()) {
            binding.brandName.setError("Vehicle Brand is required");
            binding.brandName.requestFocus();
        } else if (vehicleModel.isEmpty()) {
            binding.modelNo.setError("Model No is required");
            binding.modelNo.requestFocus();

        } else if (vehicleReg.isEmpty()) {
            binding.VehicleReg.setError("Vehicle No is required");
            binding.VehicleReg.requestFocus();

        }else if (cityinfo.isEmpty()) {
            binding.cityinfo.setError("City is required");
            binding.cityinfo.requestFocus();

        }else  if(stringPhotoPath == null)
        {
            Toast.makeText(requireContext(), "please select image", Toast.LENGTH_SHORT).show();
        }
        else {

            Log.d("TOKENEEE","ok"+App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN));
            Toast.makeText(requireContext(), "tokn"+App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN), Toast.LENGTH_SHORT).show();
            new Mvvm().applyForDriver(requireActivity(),
                    App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN),
                    CommonUtils.getRequestBodyText(CommonUtils.getUserId()),
                    CommonUtils.getRequestBodyText(selectedCar),
                    CommonUtils.getRequestBodyText(cityinfo),
                    CommonUtils.getRequestBodyText(brandNameCar),
                    CommonUtils.getRequestBodyText(vehicleModel),
                    CommonUtils.getRequestBodyText(vehicleReg),
                    CommonUtils.getRequestBodyText(driverInfo),
                    CommonUtils.getFileData(stringPhotoPath, "driver_license")).observe(requireActivity(), new Observer<ConvertDriverRoot>() {
                @Override
                public void onChanged(ConvertDriverRoot profileRoot) {
                    if (profileRoot != null) {

                        if (profileRoot.getStatus() == 1) {
                            Toast.makeText(requireContext(), ""+profileRoot.getMessage(), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(requireActivity(), HomeDriverActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(requireContext(), "message_"+profileRoot.getMessage(), Toast.LENGTH_SHORT).show();

                            Toast.makeText(requireContext(), "details_"+profileRoot.getDetails(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                    }

                    if(profileRoot.getMessage().equalsIgnoreCase("request status pending"))
                    {
                        Intent i = new Intent(requireActivity(), HomeDriverActivity.class);
                        startActivity(i);
                    }


                }
            });
        }

    }

    private void setCarTypeSpinner() {

    }

    private void getCarTypes() {
        new Mvvm().carType(requireActivity(),App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN)).observe(requireActivity(), new Observer<CarTypeRoot>() {
            @Override
            public void onChanged(CarTypeRoot profileRoot) {

                if (profileRoot != null) {
                    if (profileRoot.getStatus() == 1) {
                        for (int i = 0; i < profileRoot.getDetails().size(); i++) {
                            carTypeList.add(profileRoot.getDetails().get(i).getType());
                        }
                        setcarTypeAdapter(carTypeList);

                    } else {
                        Toast.makeText(requireContext(), "get Profile Fail", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }

            }
        });
    }

    private void setcarTypeAdapter(List<String> carTypeList) {

        ArrayAdapter<String> specialityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, carTypeList);
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.cartypeSpinner.setAdapter(specialityAdapter);
        binding.cartypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCar = carTypeList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void onclick() {

        binding.convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                apiApplyForDriver();


            }
        });

        binding.licanceRel.setOnClickListener(v -> {

            ImagePicker.Companion.with(ConvertToDriversFragment.this).crop().compress(512).maxResultSize(1080, 1080).start();


        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {

            imageUri = data.getData();

            stringPhotoPath = imageUri.getPath();

            binding.cam.setImageURI(imageUri);
        } else {
            Toast.makeText(getContext(), "can't get image", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
       // requireActivity().findViewById(R.id.actionBar).setVisibility(View.GONE);

    }



}