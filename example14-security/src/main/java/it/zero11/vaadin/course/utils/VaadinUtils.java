package it.zero11.vaadin.course.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

public class VaadinUtils {

	public static Pageable toPageable(Query<?, ?> query) {
		Sort sort = null; 				
		if (query.getSortOrders().size() > 0) {		
			for (QuerySortOrder order : query.getSortOrders()) {
				Sort s = Sort.by(order.getDirection().equals(SortDirection.ASCENDING) ? 
						Sort.Direction.ASC : Sort.Direction.DESC, 
						order.getSorted());
			    sort = sort == null ? s : sort.and(s);
			}
		}
		if (sort != null)
			return PageRequest.of(query.getPage(), query.getPageSize(), sort);
		else	
			return PageRequest.of(query.getPage(), query.getPageSize());		
	}

}
