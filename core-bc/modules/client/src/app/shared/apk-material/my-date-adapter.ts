import {NativeDateAdapter} from "@angular/material"

export class MyDateAdapter extends NativeDateAdapter {

  getFirstDayOfWeek(): number {
    return 1; // Monday
  }

  parse(value: any, parseFormat: Object): Date | null {
    return super.parse(value, parseFormat);
  }

  format(date: Date, displayFormat: Object): string {
    date.setMinutes(date.getMinutes() - date.getTimezoneOffset());
    return date.toISOString().slice(0, 10);
  }
}
