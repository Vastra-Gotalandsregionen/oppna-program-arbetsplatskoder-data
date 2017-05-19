import {HostListener} from "@angular/core";

export abstract class BasePaginatorComponent {

  @HostListener('window:keydown', ['$event'])
  paginateByArrowKey($event) {
    console.log($event);
    if ($event.key === 'ArrowRight') {
      this.nextPage();
    } else if ($event.key === 'ArrowLeft') {
      this.previousPage()
    }
  }

  abstract nextPage();

  abstract previousPage();

}
