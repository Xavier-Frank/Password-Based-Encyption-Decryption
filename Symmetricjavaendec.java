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
public class Symmetricjavaendec {    
    public static void main(String[] args) throws Exception{
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

            System.out.println("The File content before encryption:");
            System.out.println("//");
            //view the file content from the console usinf FileReader method
            String path = selectedFile.getAbsolutePath();
            try {
                File file = new File (path);
    
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine())
                System.out.println(scanner.nextLine());
                
                System.out.println("//");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println();            
            //get the slected file for encryption 
        FileInputStream inFile = new FileInputStream(selectedFile);
		FileOutputStream outFile = new FileOutputStream("encryptedFile.des");

        
        //Get the passphrase
        // System.out.println("Enter the passphrase to encrypt the file:");
        JFrame frame = new JFrame();
        String password = JOptionPane.showInputDialog(frame, "Enter the Passphrase for Encryption");
        // Scanner scanner = new Scanner(System.in);

		// String password = scanner.nextLine();

		PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory secretKeyFactory = SecretKeyFactory
				.getInstance("PBEWithMD5AndTripleDES");
		SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

		byte[] salt = new byte[8];
		Random random = new Random();
		random.nextBytes(salt);

		PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
		outFile.write(salt);

		byte[] input = new byte[64];
		int bytesRead;
        try {
            while ((bytesRead = inFile.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

		byte[] output = cipher.doFinal();
		if (output != null)
			outFile.write(output);

		inFile.close();
		outFile.flush();
		outFile.close();
        // scanner.close();
        System.out.println("File successfully encrypted to encryptedFile.des file");

        }else {
            System.out.println("Error selecting file");
        }

    }    
}