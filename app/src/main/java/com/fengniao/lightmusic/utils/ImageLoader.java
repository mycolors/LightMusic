package com.fengniao.lightmusic.utils;


import com.fengniao.lightmusic.R;

import java.security.PublicKey;

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    public static final int MESSAGE_POST_RESULT = 1;

    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;


//    private static final int TAG_KEY_URI = R.id.
}
