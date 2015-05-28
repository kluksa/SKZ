/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author kraljevic
 */
public class DateParam {
  private final Date date;

  public DateParam(String dateStr) throws WebApplicationException {
    if (dateStr.isEmpty()) {
      this.date = null;
      return;
    }
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    try {
      this.date = dateFormat.parse(dateStr);
    } catch (ParseException e) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
        .entity("Couldn't parse date string: " + e.getMessage())
        .build());
    }
  }

  public Date getDate() {
    return date;
  }
}
