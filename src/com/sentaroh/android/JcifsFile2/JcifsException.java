package com.sentaroh.android.JcifsFile2;

/*
The MIT License (MIT)
Copyright (c) 2019 Sentaroh

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

*/

public class JcifsException extends Exception {
    private static final long serialVersionUID = 5966004473207908418L;
    private Throwable mException = null;
    private int mNtStatus = 0;
    private String mMessage = null;
    
    public static final int NT_STATUS_INT_INVALID_JCIFS_LEVEL=0xfffffffe;
    public static final String NT_STATUS_DESC_INVALID_JCIFS_LEVEL="INVALID JCIFS LEVEL";

    public JcifsException(Throwable e, int nt_status, Throwable cause) {
        mException = new Exception(e.getMessage(), cause);
        mException.setStackTrace(e.getStackTrace());

        mNtStatus = nt_status;
        mMessage = e.getMessage();
    }

    public JcifsException(String msg) {
        mException = new Exception(msg);
        mMessage = msg;
        mNtStatus = -1;
    }

    public JcifsException(String msg, int nt_status) {
        mException = new Exception(msg);
        mMessage = msg;
        mNtStatus =nt_status;
    }

    public int getNtStatus() {
        return mNtStatus;
    }

    public Throwable getCause() {
        return mException.getCause();
    }

    public String getMessage() {
        return mMessage;
    }
}
