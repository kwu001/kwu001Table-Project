package com.example.androidrestaurantbooking;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidrestaurantbooking.Common.Common;
import com.example.androidrestaurantbooking.Fragments.HomeFragment;
import com.example.androidrestaurantbooking.Fragments.MenuFragment;
import com.example.androidrestaurantbooking.Model.User;
import com.firebase.ui.auth.data.model.PhoneNumber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {

       @BindView(R.id.bottom_navigation)
       BottomNavigationView bottomNavigationView;
       BottomSheetDialog bottomSheetDialog;
       CollectionReference userRef;

       AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);
        //Init
        userRef = FirebaseFirestore.getInstance().collection("user");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();


        //check intent, if login = true, enable full access
        //if login = false, the user will only access to menu
        if(getIntent() != null)
        {
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN,false);
            if (isLogin){
                //SEE the user account existed or not
                dialog.show();
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                                                 @Override
                                                 public void onSuccess(final Account account) {
                                                     if (account != null) {
                                                         DocumentReference currentUser = userRef.document(account.getPhoneNumber.toString());
                                                         currentUser.get()
                                                                 .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                         if (task.isSuccessful()) {

                                                                             DocumentSnapshot userSnapShot = task.getResult();
                                                                             if (!userSnapShot.exists())

                                                                                 showUpdateDialog(account.getPhoneNumber().toString());
                                                                         } else {
                                                                             //if user already sign in the system

                                                                             bottomNavigationView.setSelectedItemId(R.id.action_home);
                                                                             Common.currentUser = userSnapShot.toObject(User.class);
                                                                         }
                                                                         if (dialog.isShowing())
                                                                             dialog.dismiss();
                                                                     }

                                                                 });
                                                     }
                                                 }

                                                 @Override
                                                 public void onError(AccountKitError accountKitError) {
                                                     Toast.makeText(HomeActivity.this, "" + accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                                                 }
                                             }
   bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    Fragment fragment = null;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
       if(menuItem.getItemId() == R.id.action_home)
           fragment = new HomeFragment();
       else if(menuItem.getItemId() == R.id.action_Menu)
           fragment = new MenuFragment();
        return loadFragment(fragment);
    }
});


}
            private void showUpdateDialog(final String phoneNumber)
{

        //Init Dialog
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("Next Step");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView =getLayoutInflater().inflate(R.layout.layout_update_information,null);
        Button btn_register = (Button)sheetView.findViewById(R.id.btn_Register);
        TextInputEditText edit_name = (TextInputEditText)sheetView.findViewById((R.id.edit_name));
        TextInputEditText edit_address= (TextInputEditText)sheetView.findViewById((R.id.edit_address));


        btn_register.setOnClickListener(new View.OnClickListener() {
             if (!dialog.isShowing())
                     dialog.show();

                User user = new User(edit_name.getText().toString(), edit_address.getText().toString(), phoneNumber);
                userRef.document(PhoneNumber)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                                bottomSheetDialog.dismiss();
                if (dialog.isShowing())
                    dialog.dismiss();
                         Common.currentUser =user;
                bottomNavigationView.setSelectedItemId(R.id.action_home);
                                Toast.makeText(HomeActivity.this, "thank you", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bottomSheetDialog.dismiss();
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }


        }
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
            }


    private boolean loadFragment(Fragment fragment) {
        if(fragment != null)
        {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
        .commit();
        return true;
        }
        return false;
        }
}
