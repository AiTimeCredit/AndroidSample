package com.android.sample.ui.picture;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.common.http.BaseObserver;
import com.android.common.http.ObservableTransformerAsync;
import com.android.common.utils.Utility;
import com.android.sample.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 图片预览页面
 */
public class PictureActivity extends AppCompatActivity {

    public static final String IMAGE_TRANSITION_NAME = "imageTransitionName";
    private static final String IMAGE_URL = "image_url";
    private String mUrl;
    private PhotoView ivPicture;

    public static void startActivity(Activity activity, String url, View sharedElement) {
        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(IMAGE_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (sharedElement != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElement.setTransitionName(IMAGE_TRANSITION_NAME);
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, sharedElement, IMAGE_TRANSITION_NAME).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getStringExtra(IMAGE_URL);
        setContentView(R.layout.activity_picture);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ivPicture = findViewById(R.id.iv_picture);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivPicture.setTransitionName(IMAGE_TRANSITION_NAME);
        }
        ivPicture.setOnLongClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.picture_save_ask)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                            chechPermission(R.id.download)
                    )
                    .show();
            return true;
        });

        Glide.with(this)
                .asBitmap()
                .load(mUrl)
                .apply(new RequestOptions()
                        .centerInside()
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .priority(Priority.HIGH)
                        .override(Utility.getScreenWidth(this), Integer.MAX_VALUE)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                )
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(ivPicture);
    }

    private Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.download || itemId == R.id.share) {
            chechPermission(itemId);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chechPermission(final int itemId) {
        new RxPermissions(this)
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new BaseObserver<Permission>() {
                    @Override
                    protected void doOnNext(@NonNull Permission permission) {
                        if (permission.granted) { // 用户已经同意该权限
                            downloadPicture(itemId);
                        } else if (permission.shouldShowRequestPermissionRationale) {// 用户拒绝了该权限，没有选中『不再询问』
                            Snackbar.make(ivPicture, R.string.permission_denied, Snackbar.LENGTH_SHORT).show();
                        } else {// 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.permission_title)
                                    .setMessage(R.string.permission_message)
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                                            Utility.startPackageSettings(getContext())
                                    )
                                    .create()
                                    .show();
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {

                    }
                });
    }

    private void downloadPicture(int itemId) {
        Observable.just(mUrl)
                .map(url ->
                        Glide.with(getContext())
                                .asBitmap()
                                .load(url)
                                .apply(new RequestOptions()
                                        .skipMemoryCache(true)
                                        .priority(Priority.HIGH)
                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                )
                                .submit()
                                .get()
                )
                .flatMap((Function<Bitmap, ObservableSource<File>>) bitmap -> {
                    File file = saveBitmapToFile(bitmap);
                    if (!file.exists()) {
                        return Observable.error(new IllegalArgumentException("The picture failed to save."));
                    }
                    return Observable.just(file);
                })
                .compose(new ObservableTransformerAsync<>())
                .subscribe(new BaseObserver<File>() {
                    @Override
                    protected void doOnNext(@NonNull File file) {
                        if (itemId == R.id.download) {
                            Snackbar.make(ivPicture, String.format(getString(R.string.picture_save_to), file.getParent()), Snackbar.LENGTH_SHORT).show();
                        } else if (itemId == R.id.share) {
                            Uri uri = Utility.getUriForFileProvider(getContext(), file);
                            Utility.shareImage(getContext(), uri);
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {
                        Snackbar.make(ivPicture, R.string.download_failed, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private File saveBitmapToFile(Bitmap bitmap) throws Exception {
        if (bitmap == null) {
            throw new IllegalArgumentException("The picture failed to download.");
        }
        File file = getPictureFile();
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } finally {
            // 通知图库更新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }
        return file;
    }

    private File getPictureFile() throws NoSuchAlgorithmException {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new IllegalArgumentException("The directory of " + directory.getAbsolutePath() + " failed to create.");
            }
        }
        String fileName = Utility.getMd5(mUrl) + ".jpg";
        return new File(directory, fileName);
    }

    @Override
    public void onBackPressed() {
        if (ivPicture != null) {
            if (ivPicture.getScale() > 1) {
                ivPicture.setScale(1);
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

}
