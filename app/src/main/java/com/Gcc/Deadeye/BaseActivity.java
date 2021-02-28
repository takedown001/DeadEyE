package com.Gcc.Deadeye;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public abstract class BaseActivity extends AppCompatActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectToFirebase();
    }

    private void connectToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UnderMaintenance underMaintenance = dataSnapshot.getValue(UnderMaintenance.class);
                if (underMaintenance == null) return;
                if (underMaintenance.is_under_maintenance) {
                    showUnderMaintenanceDialog(underMaintenance.under_maintenance_message);
                } else {
                    dismissUnderMaintenanceDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUnderMaintenanceDialog(String underMaintenanceMessage) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).create();
            dialog.setCancelable(false);
            dialog.setButton(BUTTON_POSITIVE,
                    "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            BaseActivity.super.finish();
                        }
                    });
        }
        dialog.setMessage(underMaintenanceMessage);
        if (!this.isFinishing()) dialog.show();
    }

    private void dismissUnderMaintenanceDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

}