package van.planifolia.maid.util.time;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 获取时间工具类
 * @Author: Van.Planifolia
 * @CreateTime: 2023-02-27  16:47
 * @Version: 1.0
 */
@Slf4j
public class DateTimeUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM = "yyyy-MM";

    public static final String MM_DD = "MM-dd";

    /**
     * @param format 时间格式
     * @return 当前的LocalDateTime
     */
    public static LocalDate now(String format) {
        // 判断是否传递过来格式
        DateTimeFormatter dateTimeFormatter;
        if (StringUtils.hasText(format)) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String nowStr = now.format(dateTimeFormatter);
        return LocalDate.parse(nowStr, dateTimeFormatter);
    }

    /**
     * 获取当前时间，默认年-月-日 时:分:秒
     *
     * @param format 格式化的格式
     * @return 对应的LocalDataTime对象
     */
    public static LocalDateTime nowDataTime(String format) {
        // 判断是否传递过来格式
        DateTimeFormatter dateTimeFormatter;
        if (StringUtils.hasText(format)) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String nowStr = now.format(dateTimeFormatter);
        return LocalDateTime.parse(nowStr, dateTimeFormatter);
    }

    /**
     * 获取当前时间，默认年-月-日 时:分:秒
     *
     * @param format 格式化的格式
     * @return 对应的时间字符串
     */
    public static String nowDataTimeStr(String format) {
        // 判断是否传递过来格式
        DateTimeFormatter dateTimeFormatter;
        if (StringUtils.hasText(format)) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        return now.format(dateTimeFormatter);
    }

    /**
     * 获取当前时间，默认年-月-日 时:分:秒
     *
     * @param format 格式化的格式
     * @return 对应的时间字符串
     */
    public static String getTimeStr(String format, Date date) {
        // 判断是否传递过来格式
        DateTimeFormatter dateTimeFormatter;
        if (StringUtils.hasText(format)) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        }

        LocalDateTime now = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return now.format(dateTimeFormatter);
    }

    public static List<LocalDate> nearWeekList(String format) {
        // 判断是否传递过来格式
        DateTimeFormatter dateTimeFormatter;
        if (StringUtils.hasText(format)) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        // 获取当前时间
        LocalDate now = LocalDate.now();
        String nowStr = now.format(dateTimeFormatter);
        LocalDate today = LocalDate.parse(nowStr, dateTimeFormatter);
        List<LocalDate> nearWeek = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate tempDay = today.minusDays(i);
            nearWeek.add(tempDay);
        }
        return nearWeek;
    }

    /**
     * 切割date对象获得年月日信息
     *
     * @param date 日期对象
     * @return 得到的年月日数组
     */
    public static int[] getDateMessage(Date date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd");
        int year = Integer.parseInt(sdf1.format(date));
        int month = Integer.parseInt(sdf2.format(date));
        int day = Integer.parseInt(sdf3.format(date));
        return new int[]{year, month, day};
    }

    /**
     * 计算年龄差，从目标时间到今天
     *
     * @param now  今天
     * @param date 目标时间
     * @return 计算出来的时间差
     */
    public static Integer ageDifference(Date now, Date date) {
        int[] dateMessageNow = getDateMessage(now);
        int[] dateMessage = getDateMessage(date);
        /*
         * 计算年龄差，优先计算年，然后计算月，最后计算天。
         * 1.先计算出二者的year差得到结果暂且保存
         * 2.计算二者的month差，若date大于now则year减一
         * 3.若date小于now则继续计算day，若date大于now则year减一
         */
        int age = dateMessageNow[0] - dateMessage[0];
        if (dateMessage[1] > dateMessageNow[1]) {
            age--;
        } else {
            if (dateMessage[1] == dateMessageNow[1] && dateMessage[2] > dateMessageNow[2]) {
                age--;
            }
        }
        return age;
    }

    /**
     * 进行日期延后操作
     *
     * @param start     开始时间
     * @param delayDays 延后日期
     * @return 延后结果
     */
    public static Date delayedSomeDay(Date start, long delayDays) {
        long startTs = start.getTime();
        long delayTs = delayDays * 24 * 60 * 60 * 1000 + startTs;
        return new Date(delayTs);
    }

    /**
     * 计算某时间格式对应的秒数
     *
     * @param timeUnit 时间格式
     * @param num      时间格式对应的数值
     * @return 计算得到的秒数
     */
    public static long calculationToSecond(TimeUnit timeUnit, long num) {
        return timeUnit.toSeconds(num);
    }

    /**
     * 计算相差时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param timeUnit  时间格式
     * @return 计算结果
     */
    public static long calculateDifference(Date startDate, Date endDate, TimeUnit timeUnit) {
        // 计算两个时间之间的毫秒差
        long diffInMillies = endDate.getTime() - startDate.getTime();
        // 使用TimeUnit将毫秒差转换为指定的单位
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * 解析时间字符串为指定格式的Date
     * @param dateStr 时间字符串
     * @param format 格式化格式
     * @return 解析结果
     */
    public static Date parseDateStr(String dateStr,String format) {
        SimpleDateFormat sdf;
        try {
            if (Strings.isNotEmpty(format)){
                sdf=new SimpleDateFormat(format);
            }else {
                sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            }
        }catch (Exception e){
            log.info("错误的格式化格式！堆栈消息:{}",e.getMessage());
            throw new RuntimeException("错误的格式化格式！");
        }
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            log.info("解析时间失败！堆栈消息:{}",e.getMessage());
            throw new RuntimeException("解析时间失败！");
        }

    }
}