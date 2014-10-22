package de.fraunhofer.iao.muvi.managerweb.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import de.fraunhofer.iao.muvi.managerweb.logic.ScreenIDCalculator;

@XmlRootElement(name = "screenID")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScreenID implements Comparable<ScreenID> {

	@XmlAttribute
	private Integer id;

	@XmlAttribute
	private Integer row;
	
	@XmlAttribute
	private Integer column;

	public ScreenID() {

	}

	public ScreenID(int id) {
		this.setId(id);
	}

	public ScreenID(int column, int row) {
		this.setColumn(column);
		this.setRow(row);
	}

	public Integer getId() {
		if (this.column != null && this.row != null) {
			this.id = ScreenIDCalculator.getIDFromColumnAndRow(this.column, this.row);
		}
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getColumn() {
		if (this.id != null) {
			this.column = ScreenIDCalculator.getColumnFromId(this.id);
		}
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public Integer getRow() {
		if (this.id != null) {
			this.row = ScreenIDCalculator.getRowFromId(this.id);
		}
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return "" + this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ScreenID) {
			return id.equals(((ScreenID) obj).getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.id == null) {
			this.id = ScreenIDCalculator.getIDFromColumnAndRow(this.column, this.row);
		}
		return this.id;
	}

	@Override
	public int compareTo(ScreenID o) {
		if (o != null && o.getId() != null) {
			return id.compareTo(o.getId());
		}
		return 0;
	}

}
