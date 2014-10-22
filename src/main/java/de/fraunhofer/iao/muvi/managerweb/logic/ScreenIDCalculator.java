package de.fraunhofer.iao.muvi.managerweb.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ScreenIDCalculator {
	
	public static final int NUMBER_OF_COLUMNS = 6;
	public static final int NUMBER_OF_ROWS = 6;
	public static final int NUMBER_OF_SCREENS = NUMBER_OF_COLUMNS * NUMBER_OF_ROWS;
	
	private static final Log log = LogFactory.getLog(ScreenIDCalculator.class);

	public static int getIDFromColumnAndRow(int column, int row) {
		
		if (column < 1 || column > NUMBER_OF_COLUMNS || row < 1 || row > NUMBER_OF_ROWS) {
			throw new IllegalArgumentException("Column must be between 1 and " + NUMBER_OF_COLUMNS + ", rows must be between 1 and " + NUMBER_OF_ROWS + ".");
		}
		
		return NUMBER_OF_COLUMNS * (row - 1) + column;
	}
	
	public static int getColumnFromId(int id) {
		
		if (id < 1 || id > NUMBER_OF_SCREENS) {
			throw new IllegalArgumentException("ID must be between 1 and " + NUMBER_OF_SCREENS + ".");
		}
		
		return 1 + (id - 1) % NUMBER_OF_COLUMNS;
	}
	
	public static int getRowFromId(int id) {
		
		if (id < 1 || id > NUMBER_OF_SCREENS) {
			throw new IllegalArgumentException("ID must be between 1 and " + NUMBER_OF_SCREENS + ".");
		}
		
		return 1 + ((id - 1) / NUMBER_OF_COLUMNS);
		
	}
	
	public static ScreenID addColumns(ScreenID start, int columnsToAdd) {
		ScreenID end = new ScreenID();
		if (start.getColumn() + columnsToAdd > NUMBER_OF_COLUMNS) {
			throw new IllegalArgumentException("Cannot add " + columnsToAdd + " columns when start column is " + start.getColumn());
		}
		end.setId(start.getId() + columnsToAdd);
		return end;
	}
	
	public static ScreenID addRows(ScreenID start, int rowsToAdd) {
		ScreenID end = new ScreenID();
		if (start.getRow() + rowsToAdd > NUMBER_OF_ROWS) {
			throw new IllegalArgumentException("Cannot add " + rowsToAdd + " rows when start row is " + start.getRow());
		}
		end.setId(start.getId() + NUMBER_OF_COLUMNS * rowsToAdd);
		return end;
	}
	
	/**
	 * Given a rectangle defined by its start position, width and height, returns the
	 * list of ScreenID covered by the rectangle. The list is sorted and contains first
	 * the screens of the first row from left to right, then of the second from left
	 * to right, etc.
	 * @param rectangle
	 * @return
	 */
	public static List<ScreenID> getScreenIDList(Rectangle rectangle) {
		List<ScreenID> list = new ArrayList<>();
		
		if (rectangle.getStart() == null || rectangle.getWidth() == null || rectangle.getHeight() == null) {
			throw new IllegalArgumentException("Rectangle must have start, width and height defined.");
		}
		
		log.debug("Get ScreenID list for rectangle that starts at " + rectangle.getStart() + ", width: " + rectangle.getWidth() + ", height: " 
				 + rectangle.getHeight());
		
		ScreenID start = new ScreenID();
		start.setId(rectangle.getStart().getId());

		Integer width = rectangle.getWidth();
		Integer height = rectangle.getHeight();
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				list.add(addColumns(start, j));
			}
			if (i < height - 1) {
				start = addRows(start, 1);
			}
		}
		
		return list;
	}
	
	/**
	 * If the given ScreenID list matches to a rectangle, the matching rectangle is returned. Otherwise,
	 * an IllegalArgumentException is thrown.
	 * @param list
	 * @return
	 */
	public static Rectangle tryToGetRectangleFromListOfScreenIDs(List<ScreenID> list) {

		if (Utils.isEmpty(list)) {
			throw new IllegalArgumentException("An empty list was sent. A rectangle is made of at least 1 screen.");
		} else if (list.size() == 1) {
			return new Rectangle(list.get(0), 1, 1);
		} else {
			
			/* First try to build a rectangle around the list */
			
			Collections.sort(list);
			ScreenID start = list.get(0);
			int startRow = start.getRow();
			int startCol = start.getColumn();
			
			int endRow = startRow;
			int endCol = startCol;
			
			for (ScreenID screen : list) {
				if (screen.getRow() > endRow) {
					endRow = screen.getRow();
				}
				if (screen.getColumn() > endCol) {
					endCol = screen.getColumn();
				}
			}
			
			int width = 1 + endCol - startCol;
			int height = 1 + endRow - startRow;
			
			Rectangle possibleRectangle = new Rectangle(start, height, width);
			
			/* Then check if we have all the screens in the rectangle and only the screens in the rectangle */
			List<ScreenID> screensInRectangle = ScreenIDCalculator.getScreenIDList(possibleRectangle);
			
			if (screensInRectangle.size() == list.size()) {
				
				for (int i=0; i<screensInRectangle.size(); i++) {
					if (!screensInRectangle.get(i).equals(list.get(i))) {
						throw new IllegalArgumentException("The screen " + screensInRectangle.get(i) + " was expected but not at the right place in the given list.");
					}
				}
				
				return possibleRectangle;
			} else {
				throw new IllegalArgumentException("The given ScreenID list does not match to a rectangle.");
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		log.info(getIDFromColumnAndRow(3, 5));
		log.info(getIDFromColumnAndRow(1, 1));
		log.info(getIDFromColumnAndRow(6, 6));
		
		try {
			log.info(getIDFromColumnAndRow(3, 26));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		log.info(getColumnFromId(1) + " - " + getRowFromId(1));
		log.info(getColumnFromId(36) + " - " + getRowFromId(36));
		log.info(getColumnFromId(23) + " - " + getRowFromId(23));
		
		try {
			log.info(getColumnFromId(37) + " - " + getRowFromId(37));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		Rectangle rectangle = new Rectangle();
		ScreenID start = new ScreenID();
		start.setId(14);
		rectangle.setStart(start);
		rectangle.setWidth(3);
		rectangle.setHeight(2);
		List<ScreenID> list = getScreenIDList(rectangle);
		for (ScreenID screenID : list) {
			log.info(screenID.getId());
		}
		
		try {
			Rectangle rectangle2 = new Rectangle();
			ScreenID start2 = new ScreenID();
			start2.setId(17);
			rectangle2.setStart(start2);
			rectangle2.setWidth(3);
			rectangle2.setHeight(2);
			getScreenIDList(rectangle2);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		
		List<ScreenID> rectList = new ArrayList<>();
		rectList.add(new ScreenID(1));
		rectList.add(new ScreenID(2));
		rectList.add(new ScreenID(7));
		rectList.add(new ScreenID(8));
		System.out.println(tryToGetRectangleFromListOfScreenIDs(rectList));
		
		List<ScreenID> rectList2 = new ArrayList<>();
		rectList2.add(new ScreenID(1));
		rectList2.add(new ScreenID(2));
		rectList2.add(new ScreenID(7));
		System.out.println(tryToGetRectangleFromListOfScreenIDs(rectList2));
		
	}
	
	
	
}
