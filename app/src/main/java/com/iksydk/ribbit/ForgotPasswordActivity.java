package com.iksydk.ribbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ForgotPasswordActivity extends Activity
{
    @InjectView(R.id.emailEditText) EditText mEmailEditText;
    @InjectView(R.id.forgotPasswordButton) Button mForgotPasswordButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.inject(this);

        showProgressBar(false);

        mForgotPasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = mEmailEditText.getText()
                        .toString();

                if(email.isEmpty())
                {
                    //error dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                    builder.setMessage(getString(R.string.forgot_password_error_message))
                            .setTitle(getString(R.string.forgot_password_error_title))
                            .setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    mForgotPasswordButton.setEnabled(false);
                    showProgressBar(true);
                    //submit forgot password to parse
                    ParseUser.requestPasswordResetInBackground(email,
                            new RequestPasswordResetCallback()
                            {
                                public void done(ParseException e)
                                {
                                    mForgotPasswordButton.setEnabled(true);
                                    showProgressBar(false);
                                    if(e == null)
                                    {
                                        // An email was successfully sent with reset instructions.
                                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        // Something went wrong. Look at the ParseException to see what's up.
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                        builder.setMessage(e.getMessage())
                                                .setTitle(getString(R.string.forgot_password_error_title))
                                                .setPositiveButton(android.R.string.ok, null);

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void showProgressBar(boolean show)
    {
        if(show)
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
