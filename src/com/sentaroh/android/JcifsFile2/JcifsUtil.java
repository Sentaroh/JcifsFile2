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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcifsUtil {

	private static final Logger log = LoggerFactory.getLogger(JcifsUtil.class);
	
    final static public boolean isValidIpV4Address(String in_addr) {
        boolean result = false;
        String strip_addr = in_addr;
        if (in_addr.indexOf(":") >= 0) strip_addr = in_addr.substring(0, in_addr.indexOf(":"));
        String[] addr = strip_addr.split("\\.");
        if (addr.length == 4) {
            boolean error = false;
            for (int i = 0; i < 4; i++) {
                try {
                    int num = Integer.parseInt(addr[i]);
                    if (num < 0 || num > 255) {
                        error = true;
                        break;
                    } else {
                        if (i > 2) {
                            if (addr[i].startsWith("0")) {
                                error = true;
                                break;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    error = true;
                    break;
                }
            }
            if (!error) result = true;
        }
        return result;
    }

    final static public String getSmbHostIpAddressByHostName(String smb_level, String hn) {
        if (smb_level.equals(JcifsAuth.JCIFS_FILE_SMB1)) {
            return getSmbHostIpAddressFromNameSmb1(hn);
        } else if (smb_level.equals(JcifsAuth.JCIFS_FILE_SMB23)) {
            return getSmbHostIpAddressFromNameSmb23(hn);
        }
        return null;
    }

    final static private String getSmbHostIpAddressFromNameSmb1(String hn) {
        String ipAddress = null;
        try {
            jcifs13.netbios.NbtAddress nbtAddress = jcifs13.netbios.NbtAddress.getByName(hn);
            InetAddress address = nbtAddress.getInetAddress();
            ipAddress = address.getHostAddress();
        } catch (UnknownHostException e) {
        	log.error("getSmbHostIpAddressFromNameSmb1 UnknownHostException", e);
        } catch (Exception e) {
        	log.error("getSmbHostIpAddressFromNameSmb1 Exception", e);
        }
        return ipAddress;
    }

    final static private String getSmbHostIpAddressFromNameSmb23(String hn) {
        String ipAddress = null;
        try {
            jcifs.context.BaseContext bc = new jcifs.context.BaseContext(new jcifs.config.PropertyConfiguration(System.getProperties()));
            ipAddress = bc.getNameServiceClient().getByName(hn).getHostAddress();
        } catch (UnknownHostException e) {
			log.error("getSmbHostIpAddressFromNameSmb23 UnknownHostException", e);
        } catch (jcifs.CIFSException e) {
        	log.error("getSmbHostIpAddressFromNameSmb23 CIFSException", e);
        } catch (Exception e) {
        	log.error("getSmbHostIpAddressFromNameSmb23 Exception", e);
        }
        return ipAddress;
    }

    final static public boolean canIpAddressAndPortConnectable(String address, int port, int timeout) {
        boolean reachable = false;
        Socket socket = new Socket();
        try {
            socket.bind(null);
            socket.connect((new InetSocketAddress(address, port)), timeout);
            reachable = true;
            socket.close();
        } catch (IOException e) {
//        	e.printStackTrace();
        } catch (Exception e) {
//        	e.printStackTrace();
        }
        return reachable;
    }

    final static public String getSmbHostNameByAddress(String smb_level, String address) {
        if (smb_level.equals(JcifsAuth.JCIFS_FILE_SMB1)) {
            return getSmbHostNameFromAddressSmb1(address);
        } else if (smb_level.equals(JcifsAuth.JCIFS_FILE_SMB23)) {
            return getSmbHostNameFromAddressSmb23(address);
        }
        return null;
    }

    final static private String getSmbHostNameFromAddressSmb1(String address) {
        String srv_name = "";
        try {
            jcifs13.netbios.NbtAddress[] uax = jcifs13.netbios.NbtAddress.getAllByAddress(address);
            if (uax != null) {
                for (int i = 0; i < uax.length; i++) {
                    jcifs13.netbios.NbtAddress ua = uax[i];
                    String hn;
                    hn = ua.firstCalledName();
                    if (ua.getNameType() == 32) {
                        srv_name = hn;
                        break;
                    }
                }
            }
        } catch (UnknownHostException e) {
        	log.error("getSmbHostNameFromAddressSmb1 UnknownHostException", e);
        } catch (Exception e) {
        	log.error("getSmbHostNameFromAddressSmb1 Exception", e);
        }
        return srv_name;
    }

    final static private String getSmbHostNameFromAddressSmb23(String address) {
        String srv_name = "";
        try {
            jcifs.context.BaseContext bc = new jcifs.context.BaseContext(new jcifs.config.PropertyConfiguration(System.getProperties()));
            jcifs.NetbiosAddress[] uax = bc.getNameServiceClient().getNbtAllByAddress(address);
            if (uax != null) {
                for (int i = 0; i < uax.length; i++) {
                    jcifs.NetbiosAddress ua = uax[i];
                    String hn;
                    hn = ua.firstCalledName();
                    if (ua.getNameType() == 32) {
                        srv_name = hn;
                        break;
                    }
                }
            }
        } catch (UnknownHostException e) {
        	log.error("getSmbHostNameFromAddressSmb23 UnknownHostException", e);
        } catch (jcifs.CIFSException e) {
        	log.error("getSmbHostNameFromAddressSmb23 CIFSException", e);
        } catch (Exception e) {
        	log.error("getSmbHostNameFromAddressSmb23 Exception", e);
        }
        return srv_name;
    }

    final static public boolean isNetbiosAddress(String smb_level, String address) {
        if (smb_level.equals(JcifsAuth.JCIFS_FILE_SMB1)) {
            return isNbtAddressActiveSmb1(address);
        } else if (smb_level.equals(JcifsAuth.JCIFS_FILE_SMB23)) {
            return isNbtAddressActiveSmb214(address);
        }
        return false;
    }

    final static private boolean isNbtAddressActiveSmb1(String address) {
        boolean result = false;
        try {
            jcifs13.netbios.NbtAddress na = jcifs13.netbios.NbtAddress.getByName(address);
            result = na.isActive();
        } catch (UnknownHostException e) {
        	log.error("isNbtAddressActiveSmb1 UnknownHostException", e);
        } catch (Exception e) {
        	log.error("isNbtAddressActiveSmb1 Exception", e);
        }
        return result;
    }

    final static private boolean isNbtAddressActiveSmb214(String address) {
        boolean result = false;
        try {
            jcifs.context.BaseContext bc = new jcifs.context.BaseContext(new jcifs.config.PropertyConfiguration(System.getProperties()));
            jcifs.NetbiosAddress na = bc.getNameServiceClient().getNbtByName(address);
            result = na.isActive(bc);
        } catch (UnknownHostException e) {
        	log.error("isNbtAddressActiveSmb2 UnknownHostException", e);
        } catch (jcifs.CIFSException e) {
        	log.error("isNbtAddressActiveSmb2 CIFSException", e);
        } catch (Exception e) {
        	log.error("isNbtAddressActiveSmb2 Exception", e);
        }
        return result;
    }

    final static public String[] analyzeNtStatusCode(JcifsException e, String url, String user) {
        String[] result = new String[4];

        String host_t1 = url.replace("smb://", "");
        String host_id = host_t1;
        String share_name = "";
        String file_path = "";
        if (host_t1.indexOf("/") >= 0) {
            host_id = host_t1.substring(0, host_t1.indexOf("/"));
            String share_t1 = host_t1.replace(host_id + "/", "");
            if (share_t1.indexOf("/") >= 0) {
                share_name = share_t1.substring(0, share_t1.indexOf("/"));
                String fpath_t1 = share_t1.replace(share_name + "/", "");
                file_path = fpath_t1;
            }
        }

        result[1] = host_id;
        result[2] = share_name;
        result[3] = file_path;

        String msg_text = e.getMessage();

        result[0] = msg_text + "\n" +
                "User=" + user + String.format(", NT Status 0x%h", e.getNtStatus()) + "\n" + "share=" + share_name + ", path=" + file_path;
        return result;
    }

}
