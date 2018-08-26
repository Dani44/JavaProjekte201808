package wwr.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import wwr.model.enities.Review;
import wwr.model.enities.ReviewAttachment;

public interface ReviewAttachmentRepository extends CrudRepository<ReviewAttachment,Long>{
	public Iterable<ReviewAttachment> findAll() ;
	public Iterable<ReviewAttachment> findByReview( Review review) ;
	public Optional<ReviewAttachment> findById(Long id) ;	
}

