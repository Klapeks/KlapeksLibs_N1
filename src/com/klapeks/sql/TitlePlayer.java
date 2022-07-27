package com.klapeks.sql;

import java.util.List;

import com.klapeks.sql.anno.Column;
import com.klapeks.sql.anno.IfYaml;
import com.klapeks.sql.anno.Limit;
import com.klapeks.sql.anno.Primary;
import com.klapeks.sql.anno.PrimaryConstraint;
import com.klapeks.sql.anno.Table;

@Table("m_cosmetic_titles")
@IfYaml("~H:/_Niobium/m_titles.yml")
@PrimaryConstraint("TL_plcat")
public class TitlePlayer {
	
	@Column("player")
	@Limit(32)
	@Primary
	String player;
	
	@Column("category")
	@Limit(64)
	@Primary
	String category;
	
	@Column("titles")
	List<String> titles;
	
}