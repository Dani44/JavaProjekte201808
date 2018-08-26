package wwr.model.enities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonManagedReference;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name="Recipies")
public class Recipe {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="reci_id" )
	private Long id ;
	
	// Sample: {"id"="1" , "nickname"="swissman"}
	@ManyToOne(fetch=FetchType.LAZY  )
	@JoinColumn(name="pers_id" , nullable=false , foreignKey = @ForeignKey(name="FK_RECIPE_PERSON"))
	private Person owner ;
	
	
	// Sample: Speer	
	@Column(name="reci_caliber"  , length=30 , nullable=false  )
	private String caliber ;


	// Sample: Speer	
	@Column(name="reci_bullet_mfg"  , length=30 , nullable=false  )
	private String bulletMfg ;
	
	// Sample: FMJ	
	@Column(name="reci_bullet_type" , length=15 , nullable=false )	
	private String bulletType ;

	@Column(name="reci_bullet_dia" , length=15 , nullable=false )	
	private double  bulletDia ;
	
	// Sample: 158	
	@Column(name="reci_bullet_weight_grains" , nullable=false )	
	private double bulletWeightGrains ;


	@Column(name="reci_powder_mfg" , length=30 , nullable=false)
	private String powderMfg ;
	
	// Sample: WW31	
	@Column(name="reci_powder_shortname" , length=10 , nullable=false )
	private String powderShortName ;
	
	
	@Column(name="reci_powder_min"  )	
	private double powderMin ;
	
	@Column(name="reci_powder_load"  )	
	private double powderLoad ;

	@Column(name="reci_powder_max"  )	
	private double powderMax ;
	
	// Sample: 1.095	
	@Column(name="reci_oal_inches" , nullable=true)	
	private double oalInches ;
	
	@Column(name="reci_oal_mm" , nullable=true )	
	private double oalmm ;
	
	@Column(name="reci_primertype" , length=20 , nullable=true )
	private String primerType ;
	
	@Column(name="reci_visible_state" , nullable=true )
	private Integer visibleState ;

	@Column(name="reci_note"  , length=300 , nullable=true  )
	private String note ;

	
	@OneToMany( mappedBy="recipe" , cascade = CascadeType.ALL )
	@JsonManagedReference
	private Set<Lot> lots ;

	@OneToMany( mappedBy="recipe" , cascade = CascadeType.ALL )
	@JsonManagedReference
	private Set<Review> reviews;
	
	public int getAvgRating(){
	    int iReturn = 0 ;
	    if( reviews.size() > 0 ){
		    for( Review review : reviews  ){
		    	iReturn += review.getSummary() ;
		    }
		    iReturn = iReturn / reviews.size() ;	    	
	    }
	    return iReturn ;
	}	
	
	
	public Recipe(Long id, Person owner, 
				  String caliber, 
				  String bulletMfg,
				  String bulletType, 				  
				  double bulletWeightGrains,
				  double bulletDia ,
				  String powderMfg ,
				  String powderShortName, 
				  double powderMin,
				  double powderLoad,				  
				  double powderMax,
				  double oalInches, 
				  double oalMm,
				  String primerType ,
				  Integer visibleState,
				  String note ) {
		super();
		this.id = id;
		this.owner = owner;
		this.caliber = caliber;
		this.bulletMfg = bulletMfg;
		this.bulletType = bulletType;
		this.bulletWeightGrains = bulletWeightGrains;
		this.bulletDia = bulletDia ;
		this.powderMfg  = powderMfg ;
		this.powderShortName = powderShortName;
		this.powderMin = powderMin;
		this.powderLoad = powderLoad ;
		this.powderMax = powderMax;
		this.oalInches = oalInches;
		this.oalmm = oalMm;
		this.primerType = primerType ;
		this.visibleState = visibleState;
		this.note = note ;
	}


	public Recipe() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
