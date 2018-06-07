package com.xx.gamelibrary.image;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 图片选择工具
 * 作者: LeiXiaoXing on 2016/9/30 11:26
 */


public class ImageChooseHelper {
    /**
     * 相机获取图片
     */
    private final int REQUEST_CODE_PHOTO = 1;
    /**
     * 图库获取图片
     */
    private final int REQUEST_CODE_ALBUM = 2;
    /**
     * 图片裁剪
     */
    private final int REQUEST_CODE_CROP = 3;
    private ImageConfigProvider imageConfigProvider;
    /**
     * 头像文件
     */
    private File file;
    /**
     * 是否开启裁剪
     * 默认开启
     */
    private boolean isCrop = true;
    /**
     * 使用弱引用保存Activity实例
     */
    private WeakReference<Activity> activityWeakReference;
    private WeakReference<FragmentActivity> fragmentActivityWeakReference;
    private WeakReference<Fragment> fragmentWeakReference;
    private OnFinishChooseImageListener listener;
    private OnFinishChooseAndCropImageListener mOnFinishChooseAndCropImageListener;

    public ImageChooseHelper(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    public ImageChooseHelper(Fragment fragment) {
        fragmentWeakReference = new WeakReference<>(fragment);
        fragmentActivityWeakReference = new WeakReference<>(fragment.getActivity());
    }

    public ImageConfigProvider getImageConfigProvider() {
        if (imageConfigProvider == null) {
            throw new NullPointerException("ImageConfigProvider 未被设置");
        }
        return imageConfigProvider;
    }

    /**
     * 设置图片操作提供者
     *
     * @param imageConfigProvider
     */
    public void setImageConfigProvider(ImageConfigProvider imageConfigProvider) {
        this.imageConfigProvider = imageConfigProvider;
    }

    /**
     * Android N下获取文件Uri的正确姿势
     *
     * @param file 文件
     * @return
     */
    private Uri getUri(File file) {
        if (file == null)
            throw new NullPointerException("文件不存在");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (getImageConfigProvider().getAuthority() == null) {
                throw new NullPointerException("Provider未路径配置");
            }
            return FileProvider.getUriForFile(getActivity(),
                    getImageConfigProvider().getAuthority(), file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 获取弱引用中的Activity实例
     *
     * @return activity
     */
    private Activity getActivity() {
        if (fragmentActivityWeakReference != null) {
            return fragmentActivityWeakReference.get();
        }
        return activityWeakReference.get();
    }

    /**
     * 初始化文件名
     */
    private File initFile(String fileName) {
        String dirPath = Environment.getExternalStorageDirectory() + "/" + getActivity().getPackageName();
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        file = new File(dir, fileName);
        return file;
    }


    /**
     * 设置是否开启裁剪
     *
     * @param isCrop
     */
    public void setCrop(boolean isCrop) {
        this.isCrop = isCrop;
    }

    /**
     * 进入相机
     */
    public void startCamearPic() {
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", false);// 全屏
        intent.putExtra("showActionIcons", false);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        // 指定调用相机拍照后照片的储存路径
        initFile(System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUri(file));

        if (fragmentWeakReference != null) {
            fragmentWeakReference.get().startActivityForResult(intent, REQUEST_CODE_PHOTO);
        } else {
            if (getActivity() != null) {
                getActivity().startActivityForResult(intent, REQUEST_CODE_PHOTO);
            }
        }

    }

    /**
     * 进入图库选图
     */
    public void startImageChoose() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");

        //进入选图
        if (fragmentWeakReference != null) {
            fragmentWeakReference.get().startActivityForResult(intent, REQUEST_CODE_ALBUM);
        } else {
            getActivity().startActivityForResult(intent, REQUEST_CODE_ALBUM);
        }


    }

    /**
     * 获取真实图片路径
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /**
     * 获取图片的ContextUri
     *
     * @param imageFile 图片文件
     * @return ContextUri
     */
    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 裁剪照片
     *
     * @param uri 裁剪uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", getImageConfigProvider().getWidth());
        intent.putExtra("aspectY", getImageConfigProvider().getHeight());

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", getImageConfigProvider().getWidth());
        intent.putExtra("outputY", getImageConfigProvider().getHeight());
        intent.putExtra("return-data", true);

        if (fragmentWeakReference != null) {
            fragmentWeakReference.get().startActivityForResult(intent, REQUEST_CODE_CROP);
        } else {
            getActivity().startActivityForResult(intent, REQUEST_CODE_CROP);
        }
    }

    /**
     * 结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_PHOTO:
                if (isCrop) {
                    startPhotoZoom(getImageContentUri(file));
                } else {
                    if (listener != null) {
                        //相机，不裁剪,直接返回Uri和照片文件
                        listener.onFinish(getImageContentUri(file), file);
                    }
                }
                break;
            case REQUEST_CODE_ALBUM:

                if (data == null) {
                    return;
                }
                if (isCrop) {
                    startPhotoZoom(data.getData());
                } else {
                    if (listener != null) {
                        //不裁剪,直接返回Uri
                        listener.onFinish(data.getData(), new File(getRealPathFromURI(data.getData())));
                    }
                }
                break;
            case REQUEST_CODE_CROP:
                if (data == null) {
                    return;
                }
                if (data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap photo = bundle.getParcelable("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.PNG, getImageConfigProvider().getCompressQuality(), baos);
                    }

                    FileOutputStream fos = null;
                    if (mOnFinishChooseAndCropImageListener != null) {

                        try {
                            if (file != null) {
                                file.getParentFile().delete();//删除照片
                            }
                            //将裁剪出来的Bitmap转换成本地文件
                            File file = initFile("image.png");
                            fos = new FileOutputStream(file);
                            fos.write(baos.toByteArray());
                            fos.flush();
                            //通知图库有更新
                            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));

                            //裁剪过后返回Bitmap,处理生成文件用来上传
                            mOnFinishChooseAndCropImageListener.onFinish(photo, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null)
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            if (baos != null)
                                try {
                                    baos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                        }
                    }

                }
                break;
        }
    }

    /**
     * 图片裁剪返回监听
     *
     * @param listener
     */
    public void setOnFinishChooseImageListener(OnFinishChooseImageListener listener) {
        this.listener = listener;
    }

    /**
     * 设置选图裁剪回调监听
     *
     * @param onFinishChooseAndCropImageListener 选图裁剪回调监听
     */
    public void setOnFinishChooseAndCropImageListener(OnFinishChooseAndCropImageListener onFinishChooseAndCropImageListener) {
        mOnFinishChooseAndCropImageListener = onFinishChooseAndCropImageListener;
    }

    /**
     * 图片操作参数提供者
     */
    public interface ImageConfigProvider {

        /**
         * 获取图片宽
         *
         * @return
         */
        int getWidth();

        /**
         * 获取图片高
         *
         * @return
         */
        int getHeight();

        /**
         * 获取压缩质量
         *
         * @return
         */
        int getCompressQuality();

        /**
         * 获取androidN的FileProvider
         *
         * @return
         */
        String getAuthority();
    }

    /**
     * 裁剪图片完成回调监听
     */
    public interface OnFinishChooseAndCropImageListener {
        void onFinish(Bitmap bitmap, File file);
    }

    /**
     * 仅选图完成回调监听
     */
    public interface OnFinishChooseImageListener {
        void onFinish(Uri uri, File file);
    }

    public abstract static class ImageConfigAdapter implements ImageConfigProvider {

        @Override
        public int getWidth() {
            return 120;
        }

        @Override
        public int getHeight() {
            return 120;
        }

        @Override
        public int getCompressQuality() {
            return 100;
        }
    }
}
