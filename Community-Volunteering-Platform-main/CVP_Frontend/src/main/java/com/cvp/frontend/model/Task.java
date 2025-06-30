// package com.cvp.frontend.model;

// import java.time.LocalDate;
// import lombok.Data;


// @Data
// public class Task {
    
//     private String title;
//     private String description;
//     private String location;
//     private String status;
//     private String priority;
//     private String category;

//     private LocalDate eventDate;
//     private LocalDate endDate; 

    
// }

package com.cvp.frontend.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Task {

    private Long id;         // ✅ Added Task ID  
    private String title;    
    private String description;
    private String location;
    private String status;
    private String priority;
    private String category;
    private LocalDate eventDate;
    private LocalDate endDate;
    private Long org_id;     // ✅ Added Organization ID  

}

