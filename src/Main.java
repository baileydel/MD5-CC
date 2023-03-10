/**
 * @author baileydel
 * @created 12/27/2022 - 11:14 PM
 * @project MD5 CC
 *
 *  A simple program I needed to make using MD5 hashes to compare 2 files together.
 *  When the comparative is determined the program will compress and store the copy.\
 *  The Original file will remain untouched.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    // Create a new Map with the Key being the hash and the Value being a List of Files.
    // There can be a lot of Files with different path's and the same Hash.
    private static final Map<String, List<File>> MAP = new HashMap<>();


    // Nullable
    public static byte[] toByteArray(File in) throws IOException {
        FileInputStream fis = new FileInputStream(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

        int byteReads;
        while ((byteReads = fis.read()) != -1) {
            output.write(byteReads);
        }
        
        fis.close();
        output.close();
        return output.toByteArray();
    }

    /**
     * Gets the MD5 hash from a file using {@link MessageDigest}
     */
    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        // Convert the file data to a byte array.
        byte[] fileData = toByteArray(file);

        // Update the algorithm
        digest.update(fileData);

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    //Gets a Directory from the user, if the directory is invalid it will ask the user again.
    private static File getDirectoryFromUser(String message) {
        File file = null;
        Scanner in = new Scanner(System.in);
        // Check if the file exists, or if it's a directory
        // Will continue to get input until parameters are met.
        while (file == null || !file.exists() || !file.isDirectory()) {
            System.out.print(message);
            // Get input from console.
            String path = in.nextLine();

            // Create a new file everytime to check the parameters
            file = new File(path);
        }
        return file;
    }

    /**
     * Traverses though a directory using recursion, then adds any files found in a HashMap.
     * @param md5Digest MD5 algorithm
     * @param file Path to search
     * @throws IOException When there's no access to the file.
     */
    private static void checkFiles(MessageDigest md5Digest, File file) throws IOException {
        // Get all files in the current directory
        File[] files = file.listFiles();

        // Don't continue if no files found.
        if (files == null) {
           return;
        }

        for (File f : files) {
            // Only use getFileChecksum on files.
            if (f.isFile()) {
                String hash = getFileChecksum(md5Digest, f);

                // Check if the map has the List
                List<File> list = MAP.get(hash);

                // If null create a new one.
                if (list == null) {
                    list = new ArrayList<>();
                }

                // Always update the list when a file is found.
                list.add(f);
                MAP.put(hash, list);

                System.out.println("[+] " + hash + ", " + f.getName());
            }
            else {
                // It's a directory, call checkFiles() again.
                checkFiles(md5Digest, f);
            }
        }
    }


    public static void main(String[] args) {
        File file = getDirectoryFromUser("Directory Path to search: ");

        try {
            // Only check directories for files.
            if (file.isDirectory()) {
                // Get MD5 Hash algorithm from MessageDigest
                MessageDigest md5Digest = MessageDigest.getInstance("MD5");

                // Initial check of path.
                checkFiles(md5Digest, file);

                // if there's duplicates of the same file
                // compress and store them in a .zip.
                if (MAP.size() > 0) {
                    File out = getDirectoryFromUser("\nOutput Path: ");
                    File zipFile = new File(out + "/Copies.zip");
                    ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()));

                    for (Map.Entry<String, List<File>> entry : MAP.entrySet()) {
                        List<File> files = entry.getValue();
                        // Continue on if there's only 1 file in the list.
                        if (files.size() <= 1) {
                            continue;
                        }

                        // Start at index 1 to skip the first file found.
                        for (int i = 1 ; i < files.size(); i++) {
                            File f = files.get(i);
                            byte[] data = toByteArray(f);

                            if (!f.isFile()) {
                                continue;
                            }

                            System.out.println("[~] Compressing " + f.getName());

                            // Create a new ZipEntry, basically tells .zip it's a chunk we are writing to.
                            zip.putNextEntry(new ZipEntry(f.getName()));

                            zip.write(data);

                            // End it.
                            zip.closeEntry();

                            if (!f.delete()) {
                                System.out.println("[!] Failed to delete " + f.getAbsolutePath());
                            }
                        }
                    }
                    zip.finish();
                    zip.close();
                }
                else {
                    System.out.println("[!] No Copies found.");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}