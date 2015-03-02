/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs.util;

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
public class DateTimeParam {
  private final Date date;

  public DateTimeParam(String dateStr) throws WebApplicationException {
    if (dateStr.isEmpty()) {
      this.date = null;
      return;
    }
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
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
