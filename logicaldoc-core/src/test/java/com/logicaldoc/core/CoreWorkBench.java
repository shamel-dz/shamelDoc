package com.logicaldoc.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import javax.mail.MessagingException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.logicaldoc.core.communication.EMail;
import com.logicaldoc.core.communication.EMailAttachment;
import com.logicaldoc.core.communication.MailUtil;
import com.logicaldoc.util.http.HttpUtil;
import com.logicaldoc.util.io.FileUtil;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.IdenticonAvatar;

public class CoreWorkBench {

	public static void main(String[] args) throws Exception {
		
		EMail email = MailUtil.messageToMail(new File("src/test/resources/email2022-00398.eml"), true);
		System.out.println(email.getAttachmentsCount());
		
//		String[] timezones = TimeZone.getAvailableIDs();
//		for (String timezone : timezones) {
//			System.out.println(timezone);
//		}
//		
//		System.out.println(ExceptionUtils.getStackTrace(new Exception()));
//
//		// Getting the current date from java.util.Date class
//		Date currentTime = new Date();
//		System.out.println("currentTime : " + currentTime);
//
//		// Date time to current timezone
//		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String ISTTime = dateFormatter.format(currentTime);
//		System.out.println("local time : " + ISTTime);
//
//		// Date time to PST
//		// Creating PST timezone
//		TimeZone pstTimezone = TimeZone.getTimeZone("America/Los_Angeles");
//
//		// setting pst timezone to formatter.
//		dateFormatter.setTimeZone(pstTimezone);
//
//		// converting IST to PST
//		String PSTTime = dateFormatter.format(currentTime);
//		System.out.println("PST time : " + PSTTime);
//
//		// Date time to GST - Dubai Gulf
//		// Creating GST timezone
//		TimeZone gstTimezone = TimeZone.getTimeZone("Asia/Dubai");
//
//		// setting pst timezone to formatter.
//		dateFormatter.setTimeZone(gstTimezone);
//
//		// converting IST to PST
//		String GSTTime = dateFormatter.format(currentTime);
//		System.out.println("GST time : " + GSTTime);
//
//		dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
//		System.out.println("Rome time : " + dateFormatter.format(currentTime));

		// getting the diff b/w two los angeles and dubai times.

		// printDurationBetweenTwoDates(losAngelesDateTime, dubaiDateTime);

//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//		Date date= df.parse("2021-11-26T10:08:55+01:00");
//		System.out.println("date1: "+date);
//
//		df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//		date= df.parse("2021-11-26T10:08:55+04:00");
//		System.out.println("date2: "+date);
//		
//		df.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
//		System.out.println("format: "+df.format(date));
//		

//		EMail email = MailUtil.msgToMail(new File("C:\\Users\\marco\\Downloads\\hebrewemail.msg"), false);
//		System.out.println("subject: " + email.getSubject());

//		avatarStuff();

//		statsStuff();

//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		Document doc1 = new Document();
//		doc1.setId(1L);
//		doc1.setFileName("doc001.pdf");
//		doc1.setDate(df.parse("2020-05-20"));
//
//		Document doc2 = new Document();
//		doc2.setId(2L);
//		doc2.setFileName("doc002.pdf");
//		doc2.setDate(df.parse("2020-05-15"));
//
//		Document doc3 = new Document();
//		doc3.setId(3L);
//		doc3.setFileName("doc003.pdf");
//		doc3.setDate(df.parse("2020-05-16"));
//		
//		Document doc4 = new Document();
//		doc4.setId(4L);
//		doc4.setFileName("doc004.pdf");
//		doc4.setDate(df.parse("2020-05-21"));
//		
//		List<Document> docs = new ArrayList<Document>();
//		docs.add(doc1);
//		docs.add(doc2);
//		docs.add(doc3);
//		docs.add(doc4);
//
//		Collections.sort(docs, DocumentComparator.getComparator("filename desc, date asc"));
//		for (Document doc : docs) {
//			System.out.println(doc.getFileName()+" > "+df.format(doc.getDate()));
//		}
		//
		// File xslt = new File("target/xslt");
		// FileUtils.copyURLToFile(
		// new
		// URL("https://www.fatturapa.gov.it/export/fatturazione/sdi/fatturapa/v1.2.1/fatturaPA_v1.2.1.xsl"),
		// xslt);

//		ZipUtil zipUtil = new ZipUtil();
//		List<String> entries = zipUtil.listEntries(new File("c:/tmp/ld831-index.zip"));
//		for (String entry : entries) {
//			System.out.println(entry);
//		}

//		String inputFile = "src/test/resources/aliceDynamic.epub";
//		Reader reader = new Reader();
//		reader.setIsIncludingTextContent(true);
//		reader.setFullContent(inputFile);
//		
//		BookSection bookSection = reader.readSection(1);
//		
//		System.out.println(bookSection.getSectionContent());
//		FileUtil.writeFile(bookSection.getSectionContent(), "c:/tmp/ebook.html");

//		emailStuff();
	}

	private static void avatarStuff() {
		Avatar avatar = IdenticonAvatar.newAvatarBuilder().size(128, 128).build();
		BufferedImage image = avatar.create(-1050);
		System.out.println(image);
	}

	static void statsStuff() throws ClientProtocolException, IOException {
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();

		// Add all statistics as parameters
		postParams.add(new BasicNameValuePair("id", "pippo"));
		postParams.add(new BasicNameValuePair("userno", "pluto"));
//		postParams.add(new BasicNameValuePair("sid", "paperino"));

		postParams.add(new BasicNameValuePair("product_release", "8.6"));
		postParams.add(new BasicNameValuePair("email", ""));
		postParams.add(new BasicNameValuePair("product", "LogicalDOC"));
		postParams.add(new BasicNameValuePair("product_name", "LogicalDOC Enterprise"));

//		postParams.add(new BasicNameValuePair("java_version", javaversion != null ? javaversion : ""));
//		postParams.add(new BasicNameValuePair("java_vendor", javavendor != null ? javavendor : ""));
//		postParams.add(new BasicNameValuePair("java_arch", javaarch != null ? javaarch : ""));
//		postParams.add(new BasicNameValuePair("dbms", "mysql"));

//		postParams.add(new BasicNameValuePair("os_name", osname != null ? osname : ""));
//		postParams.add(new BasicNameValuePair("os_version", osversion != null ? osversion : ""));
//		postParams.add(new BasicNameValuePair("file_encoding", fileencoding != null ? fileencoding : ""));

		postParams.add(new BasicNameValuePair("user_language", "en"));
		postParams.add(new BasicNameValuePair("user_country", "us"));

		// Sizing
		postParams.add(new BasicNameValuePair("users", "20"));
		postParams.add(new BasicNameValuePair("guests", "50"));
		postParams.add(new BasicNameValuePair("groups", "50"));
		postParams.add(new BasicNameValuePair("docs", "50"));
		postParams.add(new BasicNameValuePair("archived_docs", "50"));
		postParams.add(new BasicNameValuePair("folders", "50"));
		postParams.add(new BasicNameValuePair("tags", "50"));
		postParams.add(new BasicNameValuePair("versions", "50"));
		postParams.add(new BasicNameValuePair("histories", "50"));
		postParams.add(new BasicNameValuePair("user_histories", "50"));
		postParams.add(new BasicNameValuePair("votes", "50"));

		SimpleDateFormat isoDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		/*
		 * Quotas
		 */
		postParams.add(new BasicNameValuePair("docdir", "50"));
		postParams.add(new BasicNameValuePair("indexdir", "50"));
		postParams.add(new BasicNameValuePair("quota", "50"));

//		/*
//		 * Registration
//		 */
//		postParams.add(new BasicNameValuePair("reg_name", regName != null ? regName : ""));
//		postParams.add(new BasicNameValuePair("reg_email", regEmail != null ? regEmail : ""));
//		postParams.add(new BasicNameValuePair("reg_organization", regOrganization != null ? regOrganization : ""));
//		postParams.add(new BasicNameValuePair("reg_website", regWebsite != null ? regWebsite : ""));

		HttpPost post = new HttpPost("http://stat.logicaldoc.com/stats/collect");
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams, Consts.UTF_8);
		post.setEntity(entity);

		CloseableHttpClient httpclient = HttpUtil.getNotValidatingClient(60);

		// Execute request
		try (CloseableHttpResponse response = httpclient.execute(post)) {
			int responseStatusCode = response.getStatusLine().getStatusCode();
			// log status code
			if (responseStatusCode != 200)
				throw new IOException(HttpUtil.getBodyString(response));
		}
	}

	static void emailStuff() throws MessagingException, IOException {
		EMail email = MailUtil.messageToMail(CoreWorkBench.class.getResourceAsStream("/GENNAIO2020.eml"), true);
		Map<Integer, EMailAttachment> attachments = email.getAttachments();
		for (Integer index : attachments.keySet()) {
			EMailAttachment attachment = attachments.get(index);
			if (attachment.parseContent().toLowerCase().contains("compensi erogati nel mese")) {
				System.out.println(attachment.getFileName());
				System.out.println(attachment.parseContent());
			}

		}
	}

	static class Store implements Callable<Long> {
		private File file;

		private File copy;

		public Store(File file, File copy) {
			this.file = file;
			this.copy = copy;
		}

		@Override
		public Long call() throws Exception {
			FileUtil.writeFile("ciccio", file.getPath());
			System.out.println("Created file " + file.getPath());
			FileUtil.copyFile(file, copy);

			// copyFileUsingJava7Files(file, copy);
			// System.out.println("Copied file " + copy.getPath());
			FileUtil.strongDelete(file);
			FileUtil.strongDelete(copy);
			return 0L;
		}
	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

	private static void copyFileUsingChannel(File source, File dest) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		} finally {
			sourceChannel.close();
			destChannel.close();
		}
	}

	private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}
}
