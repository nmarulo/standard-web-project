package red.softn.standard.common;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class DefaultUtils {
    
    public final static String XML_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'z";
    
    public static final String WINDOWS_1252 = "windows-1252";
    
    public static final String ISO_8859_1 = "ISO-8859-1";
    
    public static final String UTF_8 = "UTF-8";
    
    private static final String PHONE_NUMBER_PATTERN = "^[0-9]{9}$"; //matches 9 numbers only
    
    private static final String EMAIL_PATTERN = "^([a-zA-Z0-9._-]+)@([a-zA-Z0-9._-]+)\\.{1}.{2,5}(\\.{1}.{2,5})?[^\\.]$";
    
    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    
    public static String sortStringArray(String splitterString) {
        String sortedStringArray = "";
        if (splitterString != null && !splitterString.isEmpty()) {
            String[] strings = splitterString.split(";");
            Arrays.sort(strings);
            for (String sortedString : strings) {
                sortedStringArray += sortedString + ";";
            }
            if (!sortedStringArray.isEmpty()) {
                sortedStringArray = sortedStringArray.substring(0, sortedStringArray.length() - 1);
            }
        }
        return sortedStringArray;
    }
    
    public static String addNumberPrefixToPhones(String phones) {
        String phonesFixed = "";
        if (phones != null && !phones.isEmpty()) {
            String[] arrayPhones = phones.split(";");
            for (String s : arrayPhones) {
                phonesFixed += "+34" + s + ";";
            }
            if (!phonesFixed.isEmpty()) {
                phonesFixed = phonesFixed.substring(0, phonesFixed.length() - 1);
            }
        }
        return phonesFixed;
    }
    
    /**
     * @deprecated Utilizar org.apache.commons.lang3.StringUtils.isEmpty()
     */
    @Deprecated
    public static Boolean isCorrectString(String stringToCheck) {
        return stringToCheck != null && !stringToCheck.trim()
                                                      .isEmpty();
    }
    
    public static String getTimestamp(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String           fecha;
        if (date == null) {
            fecha = df.format(new Date());
        } else {
            fecha = df.format(date);
        }
        return fecha.replace(' ', 'T');
    }
    
    public static String encodeBase64String(String text) {
        try {
            return encodeBase64String(text, UTF_8);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String encodeBase64String(String text, String charsetName) {
        try {
            return encodeBase64String(text.getBytes(charsetName));
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String encodeBase64String(byte[] bytes) {
        try {
            return Base64.getEncoder()
                         .encodeToString(bytes);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String decodeBase64String(String text) {
        try {
            return decodeBase64String(text, "UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String decodeBase64String(String text, String charsetName) {
        try {
            return new String(Base64.getDecoder()
                                    .decode(text), charsetName);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static byte[] decodeBase64(String text) {
        try {
            return Base64.getDecoder()
                         .decode(text);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static Timestamp addTimeToCalendarField(Timestamp date, int calendarField, int timeToAdd) {
        Timestamp result = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(calendarField, timeToAdd);
            result = new Timestamp(calendar.getTime()
                                           .getTime());
        }
        return result;
    }
    
    public static String timestampToStringUTC(Timestamp time) {
        String respuesta = null;
        if (time != null) {
            DateFormat df = new SimpleDateFormat(XML_TIMESTAMP_FORMAT);
            df.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            respuesta = df.format(time)
                          .replace("UTC", "");
        }
        return respuesta;
    }
    
    public static Timestamp stringUTCToLocalTimestamp(String timeUTC) throws ParseException {
        Timestamp nuestraFecha = null;
        if (timeUTC != null) {
            DateFormat df = new SimpleDateFormat(XML_TIMESTAMP_FORMAT);
            df.setTimeZone(TimeZone.getDefault());
            nuestraFecha = new Timestamp(df.parse(timeUTC + "UTC")
                                           .getTime());
        }
        return nuestraFecha;
    }
    
    public static String createPathByCurrentDate(String pathToStore) {
        StringBuilder resultBuilder = new StringBuilder();
        
        if (!isEmpty(pathToStore)) {
            resultBuilder.append(pathToStore);
        }
        
        Calendar calendar = Calendar.getInstance();
        String   year     = String.valueOf(calendar.get(Calendar.YEAR));
        String   month    = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        month = (month.length() == 1) ? ("0" + month) : month;
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        day = (day.length() == 1) ? ("0" + day) : day;
        String currentDatePath = File.separator + year + File.separator + month + File.separator + day;
        resultBuilder.append(currentDatePath);
        
        return resultBuilder.toString();
    }
    
    public static boolean isCorrectTelephoneNumber(String telephone) {
        return StringUtils.isNotEmpty(telephone) && telephone.matches(PHONE_NUMBER_PATTERN);
    }
    
    public static boolean isCorrectEmailAddress(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        
        return Pattern.compile(EMAIL_PATTERN)
                      .matcher(email)
                      .matches();
    }
    
    public static boolean isCorrectIPv4WithoutPort(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        
        return Pattern.compile(IPADDRESS_PATTERN)
                      .matcher(createValidIp(ip))
                      .matches();
    }
    
    public static boolean isEmpty(Object object) {
        if (object instanceof List) {
            return DefaultUtils.isEmpty((List) object);
        }
        
        if (object instanceof Map) {
            return DefaultUtils.isEmpty((Map) object);
        }
        
        if (object instanceof String) {
            return DefaultUtils.isEmpty((String) object);
        }
        
        return Objects.isNull(object);
    }
    
    public static String createValidIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return ip;
        }
        
        return StringUtils.removeEnd(StringUtils.trim(ip), ":");
    }
    
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }
    
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }
    
    /**
     * @deprecated Utilizar org.apache.commons.lang3.ArrayUtils.isEmpty()
     */
    @Deprecated
    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }
    
    /**
     * @deprecated Utilizar org.apache.commons.lang3.StringUtils.isEmpty()
     */
    @Deprecated
    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty() || string.trim()
                                                           .isEmpty();
    }
    
    public static String removeSlash(String string) {
        return StringUtils.strip(string, "/");
    }
    
    public static boolean checkNetworkConnection(String originalIp) {
        try {
            String ip = createValidIp(originalIp);
            
            return isCorrectIPv4WithoutPort(ip) && InetAddress.getByName(ip)
                                                              .isReachable(2000);
        } catch (Exception ex) {
            return false;
        }
    }
    
    public static String dateFormat(String dateParse, SimpleDateFormat datePattern, SimpleDateFormat dateFormat) throws Exception {
        return dateFormat.format(datePattern.parse(dateParse));
    }
    
    public static String dateFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        
        return simpleDateFormat.format(date);
    }
    
    public static <O> void validateDTO(O object) throws Exception {
        ValidatorFactory            factory              = Validation.buildDefaultValidatorFactory();
        Validator                   validator            = factory.getValidator();
        Set<ConstraintViolation<O>> constraintViolations = validator.validate(object);
        
        if (constraintViolations.size() > 0) {
            
            for (ConstraintViolation<O> cv : constraintViolations) {
                throw new Exception(cv.getRootBeanClass()
                                      .getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
        }
    }
    
    public static InputStream getResourceAsStream(String name) {
        return Thread.currentThread()
                     .getContextClassLoader()
                     .getResourceAsStream(name);
    }
    
    public static String getResourceToString(String name, String encoding) throws Exception {
        try (InputStream inputStream = DefaultUtils.getResourceAsStream(name)) {
            return IOUtils.toString(inputStream, encoding);
        }
    }
    
    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    public static int parseInt(Integer value) {
        if (value == null) {
            return 0;
        }
        
        return value;
    }
    
    public static int doubleToInt(Double num, boolean removeDecimal) {
        if (num == null) {
            return 0;
        }
        
        if (removeDecimal) {
            return num.intValue();
        }
        
        return (int) Math.round(num * 100);
    }
    
    public static int doubleToInt(Double num) {
        return doubleToInt(num, false);
    }
}
