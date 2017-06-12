import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'dateX'
})
export class DateXPipe implements PipeTransform {

  transform(value: any, format: string = ""): string {
    // Try and parse the passed value.
    const momentDate = moment(value);

    // If moment didn't understand the value, return it unformatted.
    if (!momentDate.isValid()) return value;

    // Otherwise, return the date formatted as requested.
    return momentDate.format(format);
  }

}
