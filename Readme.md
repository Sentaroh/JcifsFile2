# JcifsFile2

jcifsとjcifs-ngを統一的に扱うWrapper


# How to use

- SMB1 for jcifs-1.3.17(SMB1)

```
JcifsAuth auth_smb1=new JcifsAuth(JcifsFile.JCIFS_FILE_SMB1, domain, username, userpassword);
JcifsFile jf_smb1=new JcifsFile("smb://192.168.0.10/share/readme.txt", auth_smb1);

JcifsFile[] fl=jf_smb1.listFiles();

for(JcifsFile item:fl) System.out.println("file="+item.getName());
```

- SMB2/3 by jcifs-ng

```
JcifsAuth auth_smb2=new JcifsAuth(JcifsFile.JCIFS_FILE_SMB23, domain, username, userpassword);
JcifsFile jf_smb2=new JcifsFile("smb://192.168.0.10/share/readme.txt", auth_smb2);
JcifsFile[] fl=jf_smb1.listFiles();

for(JcifsFile item:fl) System.out.println("file="+item.getName());
```
# How to build
- Clone or download from GitHub
- Import by Eclipse(File -> Import...)
- Export as JAR file (exclude the jars directory)
