package red.softn.standard.jsf.common;

import org.apache.commons.io.FileUtils;
import red.softn.standard.common.DefaultUtils;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FileBasicUtils {
    
    public static void downloadFile(String fileName, String data, String encoding, boolean base64) throws Exception {
        downloadBase(new File(fileName), (file) -> {
            String dataAux = data;
            
            try {
                if (base64) {
                    dataAux = DefaultUtils.decodeBase64String(dataAux, encoding);
                }
                
                FileUtils.writeStringToFile(file, dataAux, encoding);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    
    public static void downloadFile(String fileName, String encoding, Supplier<Object> objectSupplier) throws Exception {
        downloadBase(new File(fileName), (file) -> {
            Object data = objectSupplier.get();
            
            try {
                if (data instanceof byte[]) {
                    FileUtils.writeByteArrayToFile(file, (byte[]) data);
                } else if (data instanceof String) {
                    FileUtils.writeStringToFile(file, (String) data, encoding);
                } else {
                    throw new RuntimeException("Tipo de datos no compatible.");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    
    public static void downloadFile(File file) throws Exception {
        downloadBase(file, null);
    }
    
    private static void fileDownloadProcess(File file) throws IOException {
        FacesContext        facesContext = FacesContext.getCurrentInstance();
        String              mimeTypeFile = new MimetypesFileTypeMap().getContentType(file);
        int                 inputRead;
        HttpServletResponse response;
        
        facesContext.renderResponse();
        response = (HttpServletResponse) facesContext.getExternalContext()
                                                     .getResponse();
        response.setHeader("Content-Disposition", "attachment;filename =" + file.getName());
        response.setContentType(mimeTypeFile);
        
        try (InputStream inputStream = new FileInputStream(file);
             ServletOutputStream outputStream = response.getOutputStream()) {
            
            while ((inputRead = inputStream.read()) > -1) {
                outputStream.write(inputRead);
            }
            
            outputStream.flush();
        }
        
        facesContext.responseComplete();
    }
    
    private static void downloadBase(File file, Consumer<File> consumer) throws Exception {
        try {
            if (consumer != null) {
                consumer.accept(file);
            }
            
            fileDownloadProcess(file);
        } finally {
            FileUtils.forceDelete(file);
        }
    }
}
