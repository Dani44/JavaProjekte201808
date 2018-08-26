package wwr.model.enities;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import wwr.controllers.rest.secure.ReviewAttachmentController;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name="review_attachment")
public class ReviewAttachment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="rvat_id" )
	private Long id ;


	@JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY  )
    @JoinColumn(name="rvat_rev_id" , nullable=false , foreignKey = @ForeignKey(name="FK_RVAT_REVIEW"))
	private Review review ;
	
	@Column(name="rvat_usfi_id" )
	private Long fileId ;
	
	@Column(name="rvat_mediatype" , nullable=false , length=50)
	private String mediatype;

	@Column(name="rvat_uploadfilename" , nullable=false , length=255)
	private String uploadfilename;
	
	@Column(name="rvat_filesize" )
	private Long fileSize;

	@Column(name="rvat_description" , nullable=true , length=500)
	private String description;
	
	public String getUrl(){
		return ReviewAttachmentController.getUrl(this.getFileId()) ;
	}
	
	
}
