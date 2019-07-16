package com.example.aplikasicatatan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.StringBufferInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=findViewById(R.id.list1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, tambah.class);
                Map<String, Object> data = (Map<String, Object>)adapterView.getAdapter().getItem(i);
                intent.putExtra("filename",data.get("name").toString());
                Toast.makeText(MainActivity.this, "Membuka "+data.get("name"), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> data = (Map<String, Object>)adapterView.getAdapter().getItem(i);
                tampilDialogHapus(data.get("name").toString());
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.tambahkan){
            startActivity(new Intent(this,tambah.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mengambilList();
    }

    void mengambilList(){
        File path=getDir("NOTES",MODE_PRIVATE);
        File directory = new File(path.toString());
        if(directory.exists()){
            File[] files = directory.listFiles();
            String[] filenames = new String[files.length];
            String[] dateCreated = new String[files.length];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/YYYY HH:mm");
            ArrayList<Map<String, Object>> itemDataList = new ArrayList<Map<String, Object>>();
            for(int i = 0; i < files.length; i++){
                filenames[i] = files[i].getName();
                Date lastModDate = new Date(files[i].lastModified());
                dateCreated[i] = simpleDateFormat.format(lastModDate);
                Map<String, Object> listItemMap = new HashMap<>();
                listItemMap.put("name", filenames[i]);
                listItemMap.put("date", dateCreated[i]);
                itemDataList.add(listItemMap);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, itemDataList, android.R.layout.simple_list_item_2, new String[]{"name", "date"}, new int[]{android.R.id.text1, android.R.id.text2});
            list.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    void tampilDialogHapus(final String filename){
        new AlertDialog.Builder(this).setTitle("Hapus catatan ini?").setMessage("Apakah anda yakin akan menghapus catatan ini?").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hapusFile(filename);
            }
        })
                .setNegativeButton(android.R.string.no, null).show();
    }

    void hapusFile(String filename){
        File path=getDir("NOTES",MODE_PRIVATE);
        File file = new File(path.toString(),filename);
        if(file.exists()){
            file.delete();
        }
        mengambilList();
    }
}
