package com.smk.common.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public enum ByteBufferUtils {
    // 单例
    INSTANCE;

    public void encodeUtf8CharSequence(ByteBuf byteBuf, CharSequence charSequence) {
        int writerIndex = byteBuf.writerIndex();
        byteBuf.writeInt(0);
        int length = ByteBufUtil.writeUtf8(byteBuf, charSequence);
        byteBuf.setInt(writerIndex, length);
    }


}
