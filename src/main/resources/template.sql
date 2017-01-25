#sql("getPageForAdmin")
	SELECT 
	  b.id,
	  u.nickName nickName,
	  b.createAt,
	  b.featured,
	  c.name category,
	  b.privacy,
	  b.status,
	  b.summary,
	  b.tags,
	  b.title,
	  b.views 
	FROM
	  tb_blog b 
	  LEFT JOIN tb_user u 
	    ON b.authorId = u.id 
	  LEFT JOIN tb_category c 
	    ON b.category = c.id 
#end