package designpattern.facadepattern.sample;

//抽象外观类
public class NewEncryptFacade extends AbstractEncryptFacade {
    private FileReader reader;
    private NewCipherMachine cipher;
    private FileWriter writer;
    public NewEncryptFacade()
    {
        reader = new FileReader();
        cipher = new NewCipherMachine();
        writer = new FileWriter();
    }

    @Override
    public void FileEncrypt(String fileNameSrc, String fileNameDes) {
        String plainStr = reader.Read(fileNameSrc);
        String encryptStr = cipher.Encrypt(plainStr);
        writer.Write(encryptStr, fileNameDes);
    }
}
