import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.FileChooserUI;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
public class Decryption {
    public static void main(String[] args) throws Exception {
        //creae instance to choose file
        JFileChooser fileChooser = new JFileChooser();

        //setting the initial directory for open dialog
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(fileChooser.getParent());

        if (result == JFileChooser.APPROVE_OPTION) {
            //get the selected file from user approved option
            File selectedFile = fileChooser.getSelectedFile();

            //get the name of the file
            System.out.println("The selected file is" + " : " + selectedFile.getName());

            // System.out.println("The File content before decryption:");
            System.out.println("//");

        //Get the passphrase
        // System.out.println("Enter the password to decrypt the file:");
        // Scanner scanner = new Scanner(System.in);


		// String password = scanner.nextLine();
        JFrame frame = new JFrame();
        String password = JOptionPane.showInputDialog(frame, "Enter the Passphrase for Decryption");
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory secretKeyFactory = SecretKeyFactory
				.getInstance("PBEWithMD5AndTripleDES");
		SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);


		FileInputStream fis = new FileInputStream(selectedFile);
		byte[] salt = new byte[8];
		fis.read(salt);

		PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

		Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);
		FileOutputStream fos = new FileOutputStream("decryptedFile.txt");
		byte[] in = new byte[64];
		int read;
		while ((read = fis.read(in)) != -1) {
			byte[] output = cipher.update(in, 0, read);
			if (output != null)
				fos.write(output);
		}

		byte[] output = cipher.doFinal();
		if (output != null)
			fos.write(output);

		fis.close();
		fos.flush();
		fos.close();
        System.out.println("File successfully decrypted to decryptedFile.txt file");
        }else {
        System.out.println("Error selecting file");
    }
	}
}
