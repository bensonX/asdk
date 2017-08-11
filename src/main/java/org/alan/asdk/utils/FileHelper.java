package org.alan.asdk.utils;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * 文件帮助类
 * @author Administrator
 */
@Component
public class FileHelper {
	
	Logger logger = Logger.getLogger(FileHelper.class);

	public static FileHelper getInstance(){
		return new FileHelper();
	}

	/**
	 * 获取属性
	 * @param path ClassPath下的文件目录 (根目录是SRC)
	 * @return 返回map集合
	 */
	public Map<String, String> getPropertyMap(String path) {
		Properties p = getProperties(path);
		try {
			Enumeration<?> items = p.propertyNames();
			Map<String, String> map = new HashMap<String, String>();
			while (items.hasMoreElements()) {
				Object object = items.nextElement();
				map.put(object.toString(), p.getProperty(object.toString()));
			}
			return map;
		} catch (Exception e) {
			logger.error("获取配置文件属性错误！",e);
		}
		return null;
	}
	
	/**
	 * 获取.properties文件的属性
	 * @param path 文件地址  ClassPath下的文件目录 (根目录是SRC)
	 * @return
	 */
	public Properties getProperties(String path) {
	    InputStreamReader inReader = null;
	    //使用Spring的方式获取文件
	    Resource res = new ClassPathResource(path);
	    try {
			inReader = new InputStreamReader(res.getInputStream(), "UTF-8");
	    } catch (Exception e1) {
	    	logger.error("获取配置文件错误！",e1);
	    }
	    	Properties p = new Properties();
	    try {
	    	p.load(inReader);
	    	return p;
	    } catch (Exception e) {
	    	logger.error("加载配置InputStreamReader错误！",e);
	    }
    	return null;
	}
	/**
	 * 获取绝对路径(Src目录下)
	 * @param path
	 * @return
	 */
	public String getRealPath(String path){
		String realPath = null;
		try {
			realPath = new ClassPathResource(path).getFile().getAbsolutePath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("获取绝对路径错误",e);
		}
		return realPath;
	}
	/**
	 * 把list转化为CSV文件
	 */
	 public static boolean exportCsv(File file, List<Map<String, Object>> dataList){
	        boolean isSucess=false;
	        FileOutputStream out=null;
	        OutputStreamWriter osw=null;
	        BufferedWriter bw=null;
	        try {
	            out = new FileOutputStream(file);
	            osw = new OutputStreamWriter(out);
	            bw =new BufferedWriter(osw);
	            if(dataList!=null && !dataList.isEmpty()){
	                for(Map<String, Object> data : dataList){
	                	Set<String> set = data.keySet();
	                	for (String string : set) {
	                		bw.append(data.get(string)+"").append(",");
						}
	                	bw.append("\r");
	                }
	            }
	            isSucess=true;
	        } catch (Exception e) {
	            isSucess=false;
	        }finally{
	            if(bw!=null){
	                try {
	                    bw.close();
	                    bw=null;
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } 
	            }
	            if(osw!=null){
	                try {
	                    osw.close();
	                    osw=null;
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } 
	            }
	            if(out!=null){
	                try {
	                    out.close();
	                    out=null;
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } 
	            }
	        }
	        return isSucess;
	    }
	/**
	 * 拷贝文件
	 */
	public void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			logger.error("复制文件出错",e);
		} finally {
			try {
				if (fi != null) {
					fi.close();
				}
				if (in != null) {
					in.close();
				}
				if (fo != null) {
					fo.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("复制文件出错",e);
			}
		}
	}
	/**
	 *写文件
	 */
	public void writeFile(InputStream is, String filePath) throws Exception {
		
		byte b[]= new byte[1024*1024];
		int len = 0;
		
		FileOutputStream os = new FileOutputStream(filePath);
		while((len=is.read(b))!=-1){
			os.write(b, 0, len);
		}
		os.close();
	}
	/**
	 *写文件
	 */
	public void writeFile(InputStream is, OutputStream os) throws Exception {
		
		byte b[]= new byte[1024*1024];
		int len = 0;
		
		while((len=is.read(b))!=-1){
			os.write(b, 0, len);
		}
	}
	/**
	 * 创建目录
	 * @param file
	 */
	public void createDir(File file){
		if(file!=null&&!file.exists()){
			file.mkdirs();
		} 
	}
}
