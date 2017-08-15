import {NativeDateAdapter} from "@angular/material"
import {Util} from "../../core/util/util";

export class MyDateAdapter extends NativeDateAdapter {

  getFirstDayOfWeek(): number {
    return 1; // Monday
  }

  parse(value: any): Date | null {
    return super.parse(value);
  }

  format(date: Date, displayFormat: Object): string {
    return Util.dateToString(date);
  }
}
