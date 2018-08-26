package wwr.model.enities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;

// import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ForeignKey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name="reviews")
public class Review {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="rev_id" )
	private Long id ;
		

	@ManyToOne(fetch=FetchType.LAZY )
	@JoinColumn(name="rev_reci_id" , nullable=false , foreignKey = @ForeignKey(name="FK_REV_RECIPE") )
	@JsonBackReference
	private Recipe recipe ;
	
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name="rev_date" , nullable=false )
	private Date date ;

	@Column(name="rev_gun" , nullable=true ,length=30)
	private String gun;

	@Column(name="rev_reliability" , nullable=false , columnDefinition="int default 0")
	private int reliability ;
	
	@Column(name="rev_accurace" , nullable=false , columnDefinition="int default 0")
	private int accuracy;

	@Column(name="rev_burn" , nullable=false  , columnDefinition="int default 0")
	private int burn;
	
	@Column(name="rev_power" , nullable=false  , columnDefinition="int default 0")
	private int power;

	@Column(name="rev_summary" , nullable=false  , columnDefinition="int default 0")
	private int summary;
	
	
	@Column(name="rev_note" , nullable=true ,length=500)
	private String note;
	
	@Column(name="rev_casebrand" , nullable=true ,length=30 )
	private String casebrand;

	@Column(name="rev_primer" , nullable=true ,length=30)
	private String primer;

	@Column(name="rev_crimp" , nullable=true ,length=30)
	private String crimp;
	
	
	@Column(name="rev_location" , nullable=true ,length=30)
	private String location;

	@Column(name="rev_distance" , nullable=true ,length=30)
	private String distance;
	
	@ManyToOne(fetch=FetchType.LAZY  )
	@JoinColumn(name="rev_lot" , nullable=true , foreignKey = @ForeignKey(name="FK_REVIEW_LOT"))
	private Lot lot ;
	
	@OneToMany( mappedBy="review" , cascade = CascadeType.ALL )
	private Set<ReviewAttachment> attachments;		
	
	
	
	
	
}
