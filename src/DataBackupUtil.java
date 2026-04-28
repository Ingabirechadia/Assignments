import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class DataBackupUtil {

    public static void backupAllData() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupDir = "backup_" + timestamp;
        new File(backupDir).mkdir();

        backupFile("products.dat", backupDir);
        backupFile("customers.dat", backupDir);
        backupFile("orders.dat", backupDir);
        backupFile("carts.dat", backupDir);

        System.out.println("✓ Backup created in: " + backupDir);
    }

    private static void backupFile(String filename, String backupDir) {
        File source = new File(filename);
        if (source.exists()) {
            try {
                File dest = new File(backupDir + "/" + filename);
                try (FileInputStream fis = new FileInputStream(source);
                     FileOutputStream fos = new FileOutputStream(dest)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                System.out.println("  Backed up: " + filename);
            } catch (IOException e) {
                System.err.println("  Failed to backup: " + filename);
            }
        }
    }

    public static void restoreFromBackup(String backupDir) {
        restoreFile(backupDir + "/products.dat", "products.dat");
        restoreFile(backupDir + "/customers.dat", "customers.dat");
        restoreFile(backupDir + "/orders.dat", "orders.dat");
        restoreFile(backupDir + "/carts.dat", "carts.dat");
        System.out.println("✓ Data restored from: " + backupDir);
    }

    private static void restoreFile(String sourcePath, String destPath) {
        File source = new File(sourcePath);
        if (source.exists()) {
            try {
                File dest = new File(destPath);
                try (FileInputStream fis = new FileInputStream(source);
                     FileOutputStream fos = new FileOutputStream(dest)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                System.out.println("  Restored: " + destPath);
            } catch (IOException e) {
                System.err.println("  Failed to restore: " + destPath);
            }
        }
    }
}
