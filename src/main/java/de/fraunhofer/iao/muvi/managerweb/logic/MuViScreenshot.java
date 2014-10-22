package de.fraunhofer.iao.muvi.managerweb.logic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.fraunhofer.iao.muvi.managerweb.domain.DisplayComputer;



public class MuViScreenshot {
	
	private static final Log log = LogFactory.getLog(MuViScreenshot.class);
	
	private DisplayComputerManager dcManager;
	
	private Semaphore _lock;
	
	private String screenshotResult = ("C:\\CWM\\ScreenShot\\MuVi");
	
	private int singleWidth = 1920/10;
	
	private int singleHeight = 1080/10;
	
	private int width = (singleWidth*6);
	
	private int height = (singleHeight*6);
	
	private BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	private Graphics g = result.getGraphics();
	
	private File outputfile;
	
	
	private class TakeSingleDCScreenshot extends Thread {
		private DisplayComputer  displayComputer;
		
		protected TakeSingleDCScreenshot(DisplayComputer currentDC) {
			this.displayComputer = currentDC;
		}
		@Override
		public void run() {
			if(displayComputer.isActive()){
				displayComputer.startScreenshot();
			}
		}
	}
	
	private class ScreenshotSticher extends Thread {
		
		private DisplayComputer  displayComputer;
		
		private File outputfile;
				
		protected ScreenshotSticher(DisplayComputer currentDC, File outputFile) {
			this.displayComputer = currentDC;
			this.outputfile = outputFile;
		}
		@Override
		public void run() {
			if(displayComputer.isActive()){
				int screens = displayComputer.getDcScreenCount();
				for (int i=1 ; i<=screens ; i++)
				{
					int globalID = dcManager.getGlobalScreenIDForLocalScreenID(i, displayComputer).getId();
					File image = new File (displayComputer.getScreenShotPath(), String.valueOf(i)+".png");
					BufferedImage bi = new BufferedImage(singleWidth, singleHeight, BufferedImage.TYPE_INT_RGB);
					try {
						bi = ImageIO.read(image);
					} catch (IOException e1) {
						log.error("Unable to read image "+image.getAbsolutePath()+" will be taken as blank");
					}			
					int y= (ScreenIDCalculator.getRowFromId(globalID)-1)*singleHeight;
					int x = (ScreenIDCalculator.getColumnFromId(globalID)-1)*singleWidth;
					g.drawImage(bi, x, y, null);
					
					try {
						_lock.acquire();
						ImageIO.write(result, "png", outputfile);
						_lock.release();
					} catch (IOException | InterruptedException e) {
						_lock.release();
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}
	
	public File getScreenShot(String folder,String name) throws Exception
	{
		long start = System.currentTimeMillis();
		log.debug("Start taking screen shot in " + folder + " for " + name + "...");
		
		File parentFolder = new File (screenshotResult);
		File outputFolder = checkAndCreateFolder(parentFolder, folder);
		outputfile = new File(outputFolder,name+".png");
	
		TakeSingleDCScreenshot ssDc1= new TakeSingleDCScreenshot(dcManager.getDC1());
		TakeSingleDCScreenshot ssDc2= new TakeSingleDCScreenshot(dcManager.getDC2());
		TakeSingleDCScreenshot ssDc3= new TakeSingleDCScreenshot(dcManager.getDC3());
		TakeSingleDCScreenshot ssDc4= new TakeSingleDCScreenshot(dcManager.getDC4());
		
		ssDc1.start();
		ssDc2.start();
		ssDc3.start();
		ssDc4.start();
		
		ssDc1.join();
		ssDc2.join();
		ssDc3.join();
		ssDc4.join();
		
		_lock = new Semaphore(1);
		
		log.debug("Start stitching screenshots.");
		
		ScreenshotSticher ss1 = new ScreenshotSticher(dcManager.getDC1(),outputfile);
		ScreenshotSticher ss2 = new ScreenshotSticher(dcManager.getDC2(),outputfile);
		ScreenshotSticher ss3 = new ScreenshotSticher(dcManager.getDC3(),outputfile);
		ScreenshotSticher ss4 = new ScreenshotSticher(dcManager.getDC4(),outputfile);
		ss1.start();
		ss2.start();
		ss3.start();
		ss4.start();
		ss1.join();
		ss2.join();
		ss3.join();
		ss4.join();
		
		long duration = System.currentTimeMillis() - start;
		log.debug("Done taking screenshot in " + duration + " ms.");
		
		return outputfile;

	}
	
	private File checkAndCreateFolder(File parent, String folder)throws Exception {
		if(parent.exists()){
			if(parent.canWrite()){
				File childFolder = new File (parent,folder);
				if(!childFolder.exists()){
					childFolder.mkdir();
				}
				return childFolder;
			}
			else
			{
				log.error("Unable to create folder for new screen shots, permissions denied");
				throw new Exception("Unable to access the file directory");
			}
		}
		else if(parent.mkdir()){
			return checkAndCreateFolder(parent,folder);
		}
		throw new Exception("Unable to access the file directory " + parent + "\\" + folder);
	}

	public DisplayComputerManager getDcManager() {
		return dcManager;
	}

	public void setDcManager(DisplayComputerManager dcManager) {
		this.dcManager = dcManager;
	}
		

}

