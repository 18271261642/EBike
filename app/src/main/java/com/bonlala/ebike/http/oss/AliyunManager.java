package com.bonlala.ebike.http.oss;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bonlala.ebike.app.AppApplication;

import java.io.File;
import java.io.FileOutputStream;


/**
 * created by wq on 2019/4/11
 */
public class AliyunManager {

    //    private static AliyunManager sAliyunMnager;
    private static final String ALIYUN_URL = "https://oss-cn-shenzhen.aliyuncs.com";
    private static final String TAG = AliyunManager.class.getSimpleName();
    //    private static final String FILE_NAME = "https://isportcloud.oss-cn-shenzhen.aliyuncs.com/";
    private OSSClient mOSS;
    private String mAccessKeyId, mSecretkeyId, mSecurityToken;
    private String mBucketName;
    private String mImageName;
    private callback mCallback;

    public AliyunManager(String bucketName, String accessKeyId, String secretKeyId, String securityToken, String imageName, callback callback) {
        this.mBucketName = bucketName;
        this.mAccessKeyId = accessKeyId;
        this.mSecretkeyId = secretKeyId;
        this.mSecurityToken = securityToken;
        this.mImageName = imageName;
        this.mCallback = callback;
        init();
    }
//    public static AliyunManager getInstance() {
//
//        if (sAliyunMnager == null) {
//            synchronized (AliyunManager.class) {
//                if (sAliyunMnager == null) {
////                    sAliyunMnager = new AliyunManager();
//                }
//            }
//        }
//        return sAliyunMnager;
//    }

    private void init() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        OSSStsTokenCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(mAccessKeyId, mSecretkeyId, mSecurityToken);
        mOSS = new OSSClient(AppApplication.getInstance(), ALIYUN_URL, credentialProvider, conf);
        OSSLog.enableLog();
    }


    public void downFile(String filePath, String url) {
        //下载文件。objectKey等同于objectname，表示从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        //https://isportcloud.oss-cn-shenzhen.aliyuncs.com/seedFeed1221588761909000.mp4
        GetObjectRequest get = new GetObjectRequest(mBucketName, url);
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                if (mCallback != null) {
                    mCallback.upLoadProgress(currentSize, totalSize);
                }
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        mOSS.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                //开始读取数据。

                long length = result.getContentLength();
                Log.d("PutObject", "onSuccess:" + length + "filePath:" + filePath);
                byte[] buffer = new byte[(int) length];
                int readCount = 0;
                while (readCount < length) {
                    try {
                        readCount += result.getObjectContent().read(buffer, readCount, (int) length - readCount);
                    } catch (Exception e) {
                        OSSLog.logInfo(e.toString());
                        Log.d("PutObject", e.toString() + "filePath:" + filePath);
                    }
                }
                //将下载后的文件存放在指定的本地路径。
                try {
//
//                    File file = FileUtil.getImageFile(filePath);
//                    File fileSrc = new File(file.getPath()).getParentFile();
//                    if (!fileSrc.exists()) {
//                        fileSrc.mkdirs();
//                    }
//                    String path = file.getAbsolutePath();
//                    FileOutputStream fout = new FileOutputStream(file);
//                    fout.write(buffer);
//                    fout.close();
//                    if (mCallback != null) {
//                        mCallback.upLoadSuccess(0, path);
//                    }
                } catch (Exception e) {
                    Log.d("PutObject", e.toString() + "File exception" + filePath);
                    OSSLog.logInfo(e.toString());
                    if (mCallback != null) {
                        mCallback.upLoadFailed(" ");
                    }
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientException,
                                  ServiceException serviceException) {
                if (mCallback != null) {
                    mCallback.upLoadFailed(serviceException.getMessage());
                }

            }
        });
    }

    public void upLoadFile(String filePath,int code,String fileName) {
        // Construct an upload request
        PutObjectRequest put = new PutObjectRequest(mBucketName, fileName, filePath);

        // You can set progress callback during asynchronous upload
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if (mCallback != null) {
                    mCallback.upLoadProgress(currentSize, totalSize);
                }
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        if (mOSS == null) {
            init();
        }
        OSSAsyncTask task = mOSS.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String imgPath = "https://" + mBucketName + ".oss-cn-shenzhen.aliyuncs.com/" + fileName + "?" + System.currentTimeMillis();
                mCallback.upLoadSuccess(code, imgPath);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // Request exception
                if (clientExcepion != null) {
                    // Local exception, such as a network exception
                    // LogTest.test("clientExcepion=" + clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // LogTest.test("clientExcepion=" + serviceException.getMessage());   // Service exception
                }
                mCallback.upLoadFailed("阿里云上传失败");
            }
        });

// task.cancel(); // Cancel the task

// task.waitUntilFinished(); // Wait till the task is finished
    }


    public void cancelTask() {
        if (task != null && !task.isCompleted()) {
            task.cancel();
        }
    }


    OSSAsyncTask task;

    public void upLoadVideoFile(String filePath) {

        // Construct an upload request
        PutObjectRequest put = new PutObjectRequest(mBucketName, mImageName, filePath);

        // You can set progress callback during asynchronous upload
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if (mCallback != null) {
                    mCallback.upLoadProgress(currentSize, totalSize);
                }
               /* if(currentSize>totalSize/2){
                    task.cancel();
                }*/
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        if (mOSS == null) {
            init();
        }
        task = mOSS.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String imgPath = "https://" + mBucketName + ".oss-cn-shenzhen.aliyuncs.com/" + mImageName;
                mCallback.upLoadSuccess(0, imgPath);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // Request exception
                if (clientExcepion != null) {
                    // Local exception, such as a network exception
                    //LogTest.test("clientExcepion=" + clientExcepion.getMessage());
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    clientExcepion.printStackTrace();
                    //LogTest.test("clientExcepion=" + serviceException.getMessage());   // Service exception
                }
                mCallback.upLoadFailed("阿里云上传失败");
            }
        });

// task.cancel(); // Cancel the task

// task.waitUntilFinished(); // Wait till the task is finished
    }

    public void release() {

    }

    public interface callback {
        public void upLoadSuccess(int type, String imgPath);

        void upLoadFailed(String error);

        void upLoadProgress(long currentSize, long totalSize);
    }
}
