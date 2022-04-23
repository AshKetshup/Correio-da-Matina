package Server;

import java.io.Serializable;

public class login implements Serializable{
	private String username;
	private String pwd;
	
	public login(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}


/*
try {
passwd = new Register_pwd(email, password);
secure_password = passwd.getSecure_password();
salt = passwd.getSalt();
} catch (NoSuchProviderException e) {
e.printStackTrace();
} catch (NoSuchAlgorithmException e) {
e.printStackTrace();
}



private Register_pwd passwd;
    private String secure_password;
    private byte[] salt;

public class Register_pwd {

    private String secure_password;
    private String email;
    private byte[] salt;

    public Register_pwd(){}

    public Register_pwd(String email, String password) throws NoSuchProviderException, NoSuchAlgorithmException {
        //generates the salt -> random array of bytes
        this.salt = Secure_salt();
        //email serves as identifier
        this.email = email;

        this.secure_password = SecurePassword(password, salt);
    }

    public String getSecure_password(){
        return secure_password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getEmail() {
        return email;
    }

    //-> $salt = MD5(rand()) -> guardado com username para verificação da password
    //$rep = hash("SHA256",$salt,$pass);---> representação da password
    //username + salt + rep -> guardados -> email/username -> salt + pass + sha256 = rep = rep -> confirma.
    //Seguro e fiável.

    //receives the password and the random array of bytes generated in the constructor
    private static String SecurePassword(String password, byte[] salt){
        String generatedPassord = null;
        try{
            MessageDigest msgDigest = MessageDigest.getInstance("SHA256");
            msgDigest.update(salt);
            byte[] bytes = msgDigest.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for(int i=0; i<bytes.length;i++) {
                sb.append(Integer.toString((bytes[i] &0xff) + 0x100, 16).substring(1));
            }
            generatedPassord = sb.toString();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassord;
    }

    private static byte[] Secure_salt() throws NoSuchProviderException, NoSuchAlgorithmException {
        //Always use SecureRandom generator
        SecureRandom secureRandom = SecureRandom.getInstance("MD5","SUN");

        //create array for salt
        byte[] salt = new byte[16]; //size of byte array = 16

        //get a random salt
        secureRandom.nextBytes(salt); //
        return salt;
    }

}

*/