package com.example.mail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class EditProductActivity extends AppCompatActivity {

    private ImageButton backbutt;
    private TextView productcate;
    private ImageView cartadd;
    private EditText discountpriceEt,productprice,discountnote,
            productquantity,productdes,productname;
    private SwitchCompat discountswipe;
    private Button updateProductBtn;

    private String productId;

    private static final int Camera_request_code = 200;
    private static final int Storage_request_code = 300;
    private static final int Image_pick_gallery_code = 400;
    private static final int Image_pick_camera_code = 500;
    private String[] camerapermissions;
    private String[] storagepermissions;
    private Uri image_uri;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        backbutt= findViewById(R.id.backbutt);
        cartadd= findViewById(R.id.cartadd);
        productquantity= findViewById(R.id.productquantity);
        productdes= findViewById(R.id.productdes);
        productname= findViewById(R.id.productname);
        productcate= findViewById(R.id.productcate);
        productprice= findViewById(R.id.productprice);
        discountpriceEt= findViewById(R.id.discountedpriceEt);
        discountnote= findViewById(R.id.discountnote);
        discountswipe= findViewById(R.id.discountswipe);
        updateProductBtn= findViewById(R.id.updateProductBtn);
        productId = getIntent().getStringExtra("productId");

        discountpriceEt.setVisibility(View.GONE);
        discountnote.setVisibility(View.GONE);

        camerapermissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        loadProductDetails();

        discountswipe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    discountpriceEt.setVisibility(View.VISIBLE);
                    discountnote.setVisibility(View.VISIBLE);

                }
                else{
                    discountpriceEt.setVisibility(View.GONE);
                    discountnote.setVisibility(View.GONE);
                }
            }
        });

        productcate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });

        backbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        cartadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagepickDialog();
            }
        });


        updateProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    private void loadProductDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wholeseller");
        reference.child(firebaseAuth.getUid()).child("Products").child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String productId1 = ""+ snapshot.child("productId").getValue();
                        String productTitle = ""+ snapshot.child("productTitle").getValue();
                        String productdescription = ""+ snapshot.child("productdescription").getValue();
                        String productcategory = ""+ snapshot.child("productcategory").getValue();
                        String discountnote1 = ""+ snapshot.child("discountnote").getValue();
                        String productprice1 = ""+ snapshot.child("productprice").getValue();
                        String discountprice1 = ""+ snapshot.child("discountprice").getValue();
                        String productquantity1 = ""+ snapshot.child("productquantity").getValue();
                        String productIcon1 = ""+ snapshot.child("productIcon").getValue();
                        String timestamp = ""+ snapshot.child("timestamp").getValue();
                        String uid = ""+ snapshot.child("uid").getValue();
                        String discountAvailable = ""+ snapshot.child("discountAvailable").getValue();

                        if(discountAvailable.equals("true")){
                            discountswipe.setChecked(true);

                            discountpriceEt.setVisibility(View.VISIBLE);
                            discountnote.setVisibility(View.VISIBLE);
                        }
                        else{
                            discountswipe.setChecked(false);

                            discountpriceEt.setVisibility(View.GONE);
                            discountnote.setVisibility(View.GONE);
                        }
                         productname.setText(productTitle);
                        productdes.setText(productdescription);
                        productcate.setText(productcategory);
                        discountnote.setText(discountnote1);
                        productquantity.setText(productquantity1);
                        productprice.setText(productprice1);
                        discountpriceEt.setText(discountprice1);

                        try{
                            Picasso.get().load(productIcon1).placeholder(R.drawable.ic_baseline_add_shopping_cart_24).into(cartadd);
                        }
                        catch(Exception e){
                            cartadd.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String product_name,product_des,product_price,product_cate,discount_note,discount_priceEt,
            product_quantity;
    private boolean discountAvailbale=false;

    private void inputData() {
        product_name = productname.getText().toString().trim();
        product_des = productdes.getText().toString().trim();
        product_price = productprice.getText().toString().trim();
        product_cate = productcate.getText().toString().trim();
        product_quantity = productquantity.getText().toString().trim();
        discountAvailbale = discountswipe.isChecked();

        if(TextUtils.isEmpty(product_name)){
            Toast.makeText(this, "Name required", LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(product_price)){
            Toast.makeText(this, "Price required", LENGTH_SHORT).show();
            return;
        }
        if(discountAvailbale){
            discount_priceEt = discountpriceEt.getText().toString().trim();
            discount_note = discountnote.getText().toString().trim();
            if(TextUtils.isEmpty(product_price)){
                Toast.makeText(this, "Price required", LENGTH_SHORT).show();
                return;
            }

        }
        else {
            discount_priceEt="0";
            discount_note="";
        }
        updateproduct();

    }

    private void updateproduct() {
        progressDialog.setMessage("Updating product...");
        progressDialog.show();

        if (image_uri == null) {
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("productTitle", "" + product_name);
            hashMap.put("discountnote", "" + discount_note);
            hashMap.put("productdescription", "" + product_des);
            hashMap.put("productcategory", "" + product_cate);
            hashMap.put("discountAvailable", "" + discountAvailbale);
            hashMap.put("productprice", "" + product_price);
            hashMap.put("discountprice", "" + discount_priceEt);
            hashMap.put("productquantity", "" + product_quantity);


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
            ref.child(firebaseAuth.getUid()).child("Products").child(productId).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProductActivity.this, "Product updated...", LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProductActivity.this, "error", LENGTH_SHORT).show();
                        }
                    });

        } else {
            String filePathandName = "product_images/" + "" + productId;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathandName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadImageUri = uriTask.getResult();

                            if (uriTask.isSuccessful()) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("productTitle", "" + product_name);
                                hashMap.put("productdescription", "" + product_des);
                                hashMap.put("productcategory", "" + product_cate);
                                hashMap.put("discountAvailable", "" + discountAvailbale);
                                hashMap.put("discountnote", "" + discount_note);
                                hashMap.put("productprice", "" + product_price);
                                hashMap.put("discountprice", "" + discount_priceEt);
                                hashMap.put("productquantity", "" + product_quantity);
                                hashMap.put("productIcon", "" + downloadImageUri);


                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
                                ref.child(firebaseAuth.getUid()).child("Products").child(productId).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProductActivity.this, "Product updated...", LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProductActivity.this, "error", LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            Toast.makeText(EditProductActivity.this, "error", LENGTH_SHORT).show();

                        }
                    });

        }
    }
    private void categoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category")
                .setItems(Wholeseller_categories.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = Wholeseller_categories.options[which];
                        productcate.setText(category);
                    }
                })
                .show();
    }

    private void showImagepickDialog() {
        String[] options={"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (checkCameraPermission()) {
                                pickFromcamera();
                            } else {
                                requestCameraPermission();
                            }
                        }
                        else{
                            if(checkStoragePermission()){
                                pickFromgallery();
                            }
                            else{
                                requestStoragePermission();
                            }
                        }

                    }


                })
                .show();
    }

    private void pickFromgallery(){
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Image_pick_gallery_code);
    }

    private void pickFromcamera(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_imagez_title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_imagez_description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, Image_pick_camera_code);
    }

    private boolean checkStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void  requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagepermissions, Storage_request_code);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,camerapermissions , Camera_request_code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Camera_request_code: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromcamera();

                    } else {
                        Toast.makeText(this, "Camera permission necessary", LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case Storage_request_code: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromgallery();

                    } else {
                        Toast.makeText(this, "Storage permission necessary", LENGTH_SHORT).show();

                    }
                }
            }
            break;


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Image_pick_gallery_code) {
                image_uri = data.getData();
                cartadd.setImageURI(image_uri);
            } else if (requestCode == Image_pick_camera_code) {
                cartadd.setImageURI(image_uri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}
