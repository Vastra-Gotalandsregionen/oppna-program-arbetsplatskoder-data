import {NativeDateAdapter} from "@angular/material"

export class MyDateAdapter extends NativeDateAdapter {

  getFirstDayOfWeek(): number {
    return 1; // Monday
  }

  parse(value: any): Date | null {
    return super.parse(value);
  }

  format(date: Date, displayFormat: Object): string {
    date.setMinutes(date.getMinutes() - date.getTimezoneOffset());
    return date.toISOString().slice(0, 10);
  }
}
