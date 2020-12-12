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

import java.util.Properties;

public class JcifsAuth {
    final static public String JCIFS_FILE_SMB1 = "SMBv1";
    final static public String JCIFS_FILE_SMB23 = "SMBv2/3";
    private jcifs.smb.NtlmPasswordAuthentication mSmb1Auth = null;
    private jcifsng.CIFSContext mSmb23Auth = null;
    private String mSmbLevel = JCIFS_FILE_SMB23;

    private String mDomain = null, mUserName = null, mUserPass = null;

    /**
     * SMB1 or SMB2 Constructor
     *
     * @param smblevel
     * @param domain A domain name
     * @param user   A user name
     * @param pass   A password for user
     * @throws JcifsException
     */
    public JcifsAuth(String smb_level, String domain, String user, String pass) throws JcifsException {
    	init_type(smb_level, domain, user, pass, "SMB202", "SMB311", new Properties());
    }

    /**
     * SMB1 or SMB2 Constructor
     *
     * @param smblevel
     * @param domain A domain name
     * @param user   A user name
     * @param pass   A password for user
     * @param prop   Property
     * @throws JcifsException
     */
    public JcifsAuth(String smb_level, String domain, String user, String pass, Properties prop) throws JcifsException {
    	init_type(smb_level, domain, user, pass, "SMB202", "SMB311", prop);
    }

    /**
     * SMB2 Constructor
     *
     * @param smblevel
     * @param domain               A domain name
     * @param user                 A user name
     * @param pass                 A password for user
     * @param min_version          min SMB version ("SMB1" or "SMB202")
     * @param max_version          max SMB version ("SMB1" or "SMB311")
     * @throws JcifsException
     */
    public JcifsAuth(String smb_level, String domain, String user, String pass, String min_version, String max_version) throws JcifsException {
    	init_type(smb_level, domain, user, pass, min_version, max_version, new Properties());
    }

    /**
     * SMB2 Constructor
     *
     * @param smblevel   
     * @param domain               A domain name
     * @param user                 A user name
     * @param pass                 A password for user
     * @param min_version          min SMB version ("SMB1" or "SMB202")
     * @param max_version          max SMB version ("SMB1" or "SMB311")
     * @param prop   				Property
     * @throws JcifsException
     */
    public JcifsAuth(String smb_level, String domain, String user, String pass, String min_version, String max_version, Properties prop) throws JcifsException {
    	init_type(smb_level, domain, user, pass, min_version, max_version, prop);
    }

    private void init_type(String smb_level, String domain, String user, String pass, String min_version, String max_version, Properties prop) throws JcifsException {
        mSmbLevel = smb_level;
        mDomain = domain;
        mUserName = user;
        mUserPass = pass;
        if (isSmb1()) {
        	mSmb1Auth = new jcifs.smb.NtlmPasswordAuthentication(domain, user, pass);
        } else if (isSmb23()) {
            try {
                Properties prop_new = new Properties(prop);
                prop_new.setProperty("jcifs.smb.client.minVersion", min_version);
                prop_new.setProperty("jcifs.smb.client.maxVersion", max_version);

                jcifsng.context.BaseContext bc = new jcifsng.context.BaseContext(new jcifsng.config.PropertyConfiguration(prop_new));
                jcifsng.smb.NtlmPasswordAuthenticator creds = new jcifsng.smb.NtlmPasswordAuthenticator(domain, user, pass);
                mSmb23Auth = bc.withCredentials(creds);
            } catch (jcifsng.CIFSException e) {
                e.printStackTrace();
                throw new JcifsException(e, -1, e.getCause());
            }
        } else {
        	throw new JcifsException(JcifsException.NT_STATUS_DESC_INVALID_JCIFS_LEVEL);
        }
    }
    
    
    public String getSmbLevel() {
        return mSmbLevel;
    }

    public boolean isSmb1() {
        return mSmbLevel.equals(JCIFS_FILE_SMB1);
    }

    public boolean isSmb23() {
        return mSmbLevel.equals(JCIFS_FILE_SMB23);
    }

    public jcifs.smb.NtlmPasswordAuthentication getSmb1Auth() {
        return mSmb1Auth;
    }

    public jcifsng.CIFSContext getSmb214Auth() {
        return mSmb23Auth;
    }

    public String getDomain() {
        return mDomain;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserPass() {
        return mUserPass;
    }
}
