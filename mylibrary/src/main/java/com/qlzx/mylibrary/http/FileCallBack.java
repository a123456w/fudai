package com.qlzx.mylibrary.http;


import android.content.Context;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/15.
 */
public abstract class FileCallBack {


    private File file;
    private Context mContext;

    public abstract void inProgress(long current, long total);


    public FileCallBack(Context context, File file) {
        this.file=file;
        this.mContext = context;
    }


    public File parseNetworkResponse(Response response) throws IOException {
        return saveFile(response);
    }


    public File saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;


            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                OkHttpDownloadManager.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {

                        inProgress(finalSum, total);
                    }
                });
            }
            fos.flush();
            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }

        }
    }

    public abstract void onSuccess(File response);

    public abstract void onError(Exception e);

    public abstract void onAfter();

}
