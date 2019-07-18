package com.codepath.android.lollipopexercise.activities;
// Make sure to import this line at the top!
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.codepath.android.lollipopexercise.R;
import com.codepath.android.lollipopexercise.models.Contact;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    private Contact mContact;
    private ImageView ivProfile;
    private TextView tvName;
    private TextView tvPhone;
    private View vPalette;
    private Contact contact;
    private FloatingActionButton fab;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        vPalette = findViewById(R.id.vPalette);
        vPalette.setBackgroundColor(Color.WHITE);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Dial contact's number.
        // This shows the dialer with the number, allowing you to explicitly initiate the call.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + mContact.getNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
                //fab.setVisibility(View.INVISIBLE);
                exitReveal();
            }

        });






        // Extract contact from bundle
        mContact = (Contact)getIntent().getExtras().getSerializable(EXTRA_CONTACT);

        // Use Glide to get a callback with a Bitmap which can then
        // be used to extract a vibrant color from the Palette.
        // Define a listener for image loading
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivProfile.setImageBitmap(resource);
                // Set the result as the background color for `R.id.vPalette` view containing the contact's name.
                Palette palette = Palette.from(resource).generate();
                vPalette.setBackgroundColor(palette.getVibrantColor(Color.WHITE));
            }
        };

        // Store the target into the tag for the profile to ensure target isn't garbage collected prematurely
        ivProfile.setTag(target);
        // Instruct Picasso to load the bitmap into the target defined above
        Glide.with(DetailsActivity.this).load(mContact.getThumbnailDrawable()).asBitmap().centerCrop().into(target);
        // Fill views with data
        tvName.setText(mContact.getName());
        tvPhone.setText(mContact.getNumber());
        contact = (Contact) getIntent().getSerializableExtra(DetailsActivity.EXTRA_CONTACT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressLint("RestrictedApi")
    void enterReveal() {
        final View myView = findViewById(R.id.fab);

        // previously invisible view

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
            ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

       void exitReveal() {
           // previously visible view
           final View myView = findViewById(R.id.fab);
           // get the center for the clipping circle
           int cx = myView.getMeasuredWidth() / 2;
           int cy = myView.getMeasuredHeight() / 2;
           // get the initial radius for the clipping circle
           int initialRadius = myView.getWidth() / 2;
           // create the animation (the final radius is zero)
           Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
                // make the view invisible when the animation is done
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        myView.setVisibility(View.INVISIBLE);
                    }
                });
            
                // start the animation
                anim.start();



    } }
