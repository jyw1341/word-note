package com.jyw.mykanjinote;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CharacterActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 0;
    private TextView textView_kanji_contents,textView_meaning_contents,textView_sound_contents,textView_memorize_contents;
    private ImageView imageView,imageView_kanji_edit,imageView_meaning_edit,imageView_sound_edit,imageView_memorize_edit,imageView_add_example;
    private Button button_gallery,button_camera,button_internet;
    private ImageButton imageButton_delete;
    private RecyclerView recyclerView_example;

    private static final String TAG = CharacterActivity.class.getSimpleName();

    private String folderName, mCurrentPhotoPath,fileName;
    private int position;

    private ArrayList<String> charList;
    private ArrayList<String> charContentsList;
    PreferenceManager pm;

    ExampleListObject exampleListObject;
    ExampleListAdapter exampleListAdapter;

    private String key="";
    private String cx = "";
    private String qry =null;
    private String link = null;
    ArrayList<String> linkList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        checkCameraPermission();

        Intent intent = getIntent();
        folderName = intent.getStringExtra("folderName");
        position = intent.getIntExtra("position",0);

        pm = new PreferenceManager(getApplicationContext());
        charList= pm.getStringArrayPref(folderName);
        String charContents = charList.get(position);
        charContentsList = new ArrayList<String>();
        if (charContents != null) {
            try {
                JSONArray a = new JSONArray(charContents);
                for (int j = 0; j < a.length(); j++) {
                    String url = a.optString(j);
                    charContentsList.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        button_internet = findViewById(R.id.button_internet);
        button_internet.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
             new Thread(){
                 public void run(){
                     try{
                         qry = charContentsList.get(0);
                         URL url = new URL(
                                 "https://www.googleapis.com/customsearch/v1?key=" + key +"&num=10"+"&cx=" + cx +"&q=" + qry
                                         + "&searchType=image");
                         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                         conn.setRequestMethod("GET");
                         conn.setRequestProperty("Accept", "application/json");
                         BufferedReader br = new BufferedReader(new InputStreamReader(
                                 (conn.getInputStream())));

                         //The response is json
                         //Process to find URL from json
                         String output;
                         System.out.println("Output from Server .... \n");
                         while ((output = br.readLine()) != null) {
                             if (output.contains("\"link\": \"")) {
                                 link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(),
                                         output.indexOf("\","));
                                 Log.w(TAG,"/"+link);
                                 linkList.add(link);
                             }
                         }
                         conn.disconnect();

                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }
             }.start();

             new Thread(){
                 public void run(){
                     while (linkList.size()<1) {
                         try {
                             Thread.sleep(300);
                         } catch (Exception e) {
                             e.printStackTrace() ;
                         }

                     }
                     Intent intent = new Intent(getApplicationContext(), WebImageActivity.class);
                     intent.putStringArrayListExtra("linkList",linkList);
                     webImageLauncher.launch(intent);
                 }
             }.start();
             }
         });


        button_gallery = findViewById(R.id.button_gallery);
        button_gallery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //uri 사용하려면 action_open_document
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                imageLauncher.launch(intent);
            }
        });

        fileName = charContentsList.get(6);

        imageButton_delete = findViewById(R.id.imageButton_delete);
        imageButton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = charContentsList.get(6);
                Log.w(TAG,"start");
                if(!fileName.equals("")){
                    Log.w(TAG,"delete1");
                    delete(fileName);
                }
                if(!charContentsList.get(5).equals("")){
                    Log.w(TAG,"delete2");
                    charContentsList.set(5,"");
                    JSONArray a = new JSONArray();
                    for (int i = 0; i < charContentsList.size(); i++) {
                        a.put(charContentsList.get(i));
                    }
                    String contents = a.toString();
                    charList.set(position,contents);
                    pm.setStringArrayPref(folderName,charList);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_image));
                }
            }
        });

        textView_kanji_contents = findViewById(R.id.textView_kanji_contents);
        textView_meaning_contents = findViewById(R.id.textView_meaning_contents);
        textView_sound_contents = findViewById(R.id.textView_sound_contents);
        textView_memorize_contents = findViewById(R.id.textView_memorize_contents);

        textView_kanji_contents.setText(charContentsList.get(0));
        textView_meaning_contents.setText(charContentsList.get(1));
        textView_sound_contents.setText(charContentsList.get(2));
        textView_memorize_contents.setText(charContentsList.get(3));

        imageView =findViewById(R.id.imageView);
        imageView_kanji_edit = findViewById(R.id.imageView_kanji_edit);
        imageView_meaning_edit = findViewById(R.id.imageView_meaning_edit);
        imageView_sound_edit = findViewById(R.id.imageView_sound_edit);
        imageView_memorize_edit = findViewById(R.id.imageView_memorize_edit);

        String imageString = charContentsList.get(5);
        if(!imageString.equals("")){
            if(checkPermissions()) {
                Uri imageUri = Uri.parse(charContentsList.get(5));
                Picasso.get().load(imageUri).resize(1080,720).onlyScaleDown().into(imageView);
            }
            else{
                requestPermissions();
            }
        }

        if(!fileName.equals("")){
            loadImgArr(fileName);
        }

        imageView_kanji_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent(textView_kanji_contents,"한자 수정",0);
            }
        });

        imageView_meaning_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent(textView_meaning_contents,"훈독 수정",1);
            }
        });

        imageView_sound_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent(textView_sound_contents,"음독 수정",2);
            }
        });

        imageView_memorize_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { editEvent(textView_memorize_contents,"암기 수정",3);
            }
        });

        recyclerView_example = findViewById(R.id.recyclerView_example);
        exampleListObject = new ExampleListObject(getApplicationContext(),folderName,position);
        exampleListAdapter = new ExampleListAdapter(getApplicationContext(),exampleListObject.getDataList(),folderName,position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_example.setLayoutManager(linearLayoutManager);
        recyclerView_example.setAdapter(exampleListAdapter);

        imageView_add_example = findViewById(R.id.imageView_add_example);
        imageView_add_example.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CharacterActivity.this);
                builder.setTitle("예문 추가");
                final EditText editText = new EditText(getApplicationContext());
                builder.setView(editText);
                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = editText.getText().toString();
                        exampleListObject.addExample(str,position);
                        exampleListAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    public void back(View view){
        finish();
    }

    private void editEvent(TextView view,String str, int num){
        AlertDialog.Builder builder = new AlertDialog.Builder(CharacterActivity.this);
        builder.setTitle(str);
        final EditText editText = new EditText(CharacterActivity.this);
        builder.setView(editText);
        editText.setText(view.getText());
        builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                view.setText(str);

                charContentsList.set(num,str);
                JSONArray a = new JSONArray();
                for (int i = 0; i < charContentsList.size(); i++) {
                    a.put(charContentsList.get(i));
                }
                String contents = a.toString();
                charList.set(position,contents);
                pm.setStringArrayPref(folderName,charList);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void checkCameraPermission(){
        int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA);
        int permissionRead = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCamera!=PackageManager.PERMISSION_GRANTED
                ||permissionRead!=PackageManager.PERMISSION_GRANTED
                ||permissionWrite!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(CharacterActivity.this,Manifest.permission.CAMERA)){
                Toast.makeText(CharacterActivity.this,"이 기능을 실행하기 위해 권한이 필요합니다",Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(CharacterActivity.this,new String[]{
                Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE} , REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

        if( shouldProvideRationale ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CharacterActivity.this);
            builder.setTitle("알림");
            builder.setMessage("저장소 권한이 필요합니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startPermissionRequest();
                }
            });
            builder.show();
        } else {
            startPermissionRequest();
        }
    }

    private void saveImg(String imgName){
        try{
            //저장할 파일 경로
            File storageDir = new File(getFilesDir()+"/capture");
            if(!storageDir.exists())
                storageDir.mkdirs();

            String filename = imgName+".jpg";

            File file = new File(storageDir,filename);
            //??
            boolean deleted = file.delete();
            Log.w(TAG,"Delete Dup Check"+deleted);
            FileOutputStream output = null;

            try{
                output = new FileOutputStream(file);
                BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,output);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } finally {
                try{
                    assert output !=null;
                    output.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            charContentsList.set(5,"");
            charContentsList.set(6,filename);
            JSONArray a = new JSONArray();
            for (int i = 0; i < charContentsList.size(); i++) {
                a.put(charContentsList.get(i));
            }
            String contents = a.toString();
            charList.set(position,contents);
            pm.setStringArrayPref(folderName,charList);

            Toast.makeText(CharacterActivity.this,"이미지가 저장되었습니다",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(CharacterActivity.this,"저장에 실패했습니다",Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(String str){

            File file = new File(getFilesDir()+"/capture"+"/"+str);
            if(file.exists()){
                file.delete();
                charContentsList.set(6,"");
                JSONArray a = new JSONArray();
                for (int i = 0; i < charContentsList.size(); i++) {
                    a.put(charContentsList.get(i));
                }
                String contents = a.toString();
                charList.set(position,contents);
                pm.setStringArrayPref(folderName,charList);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_image));
                Toast.makeText(CharacterActivity.this,"파일이 삭제되었습니다",Toast.LENGTH_SHORT).show();
            }
    }

    private void loadImgArr(String filename){
        try{
            File storageDir = new File(getFilesDir()+"/capture");

            File file = new File(storageDir,filename);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"이미지 로드 실패",Toast.LENGTH_SHORT).show();
        }
    }


    ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()== Activity.RESULT_OK){
                Uri selectedImageUri = result.getData().getData();
                //저장된 uri에 접근 허용
                getContentResolver().takePersistableUriPermission(selectedImageUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageView.setImageURI(selectedImageUri);

                //한자 셰어드 리스트에 이미지 uri 추가
                String imageUriString = selectedImageUri.toString();
                charContentsList.set(5,imageUriString);
                charContentsList.set(6,"");
                JSONArray a = new JSONArray();
                for (int i = 0; i < charContentsList.size(); i++) {
                    a.put(charContentsList.get(i));
                }
                String contents = a.toString();
                charList.set(position,contents);
                pm.setStringArrayPref(folderName,charList);
                Log.i(TAG,"/imageSaved");
            }
        }
    });

     ActivityResultLauncher<Intent> webImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    Intent data = result.getData();
                    String str = data.getStringExtra("stringUri");
                    Uri uri = Uri.parse(str);
                    Picasso.get().load(uri).resize(1080,720).onlyScaleDown().into(imageView);
                    charContentsList.set(5,str);
                    charContentsList.set(6,"");
                    JSONArray a = new JSONArray();
                    for (int i = 0; i < charContentsList.size(); i++) {
                        a.put(charContentsList.get(i));
                    }
                    String contents = a.toString();
                    charList.set(position,contents);
                    pm.setStringArrayPref(folderName,charList);
                }
            }
        });


    ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
           @Override
           public void onActivityResult(ActivityResult result) {
               if(result.getResultCode()== Activity.RESULT_OK){
                   File file = new File(mCurrentPhotoPath);
                   try {
                       //이미지 uri 비트맵 변환
                       Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.fromFile(file));

                       if(bitmap!=null){
                           //??
                           ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                           int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);

                           Bitmap rotatedBitmap = null;
                           switch(orientation){
                               case ExifInterface.ORIENTATION_ROTATE_90 :
                                   rotatedBitmap = rotateImage(bitmap,90);
                                   break;

                               case ExifInterface.ORIENTATION_ROTATE_180 :
                                   rotatedBitmap = rotateImage(bitmap,180);
                                   break;

                               case ExifInterface.ORIENTATION_ROTATE_270 :
                                   rotatedBitmap = rotateImage(bitmap,270);
                                   break;

                               case ExifInterface.ORIENTATION_NORMAL:
                               default :
                                   rotatedBitmap = bitmap;
                           }
                           imageView.setImageBitmap(rotatedBitmap);
                           //셰어드에 저장될 이미지파일 이름
                           String fileName = charContentsList.get(0);
                           saveImg(fileName);
                       }
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }
           }
       });

    public static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
    }
}