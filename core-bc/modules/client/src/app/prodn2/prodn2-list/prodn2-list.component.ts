import {Component, OnInit} from '@angular/core';
import {JwtHttp} from '../../core/jwt-http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/operator'
import {Prodn2} from '../../model/prodn2';
import {Prodn1} from '../../model/prodn1';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-prodn2-list',
  templateUrl: './prodn2-list.component.html',
  styleUrls: ['./prodn2-list.component.scss'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1, })),
      state('out', style({opacity: 0, height: '0', 'margin-bottom': 0, padding: 0, display: 'none'})),
      transition('* => *', animate('.2s ease'))
    ])
  ]
})
export class Prodn2ListComponent implements OnInit {

  prodn1s$: Observable<Prodn1[]>;
  allProdn2s$: Observable<Prodn2[]>;
  allProdn2s: Prodn2[] = [];

  prodn2sForDisplay: Prodn2[] = [];

  selectedProdn1s: string[] = [];

  orphanProdn2s: string[] = [];

  showFilter = false;

  constructor(private http: JwtHttp) {
  }

  ngOnInit() {

    this.prodn1s$ = this.http.get('/api/prodn1').map(response => response.json());
    this.allProdn2s$ = this.http.get('/api/prodn2').map(response => response.json()).share();

    this.allProdn2s$.subscribe(prodn2s => this.allProdn2s = this.prodn2sForDisplay = prodn2s);

    this.updateProdn2sForDisplay();

    // Find Prodn2s where their N1 doesn't exist.
    this.allProdn2s$
      .delay(200) // Prioritize the listing so wait before we start this processing.
      .mergeMap((prodn2s: Prodn2[]) => prodn2s) // To single items
      .map((prodn2: Prodn2) => prodn2.n1) // Make this mapping in order to be able to make distinct after this step.
      .distinct() // Many Prodn2s share the same N1 so we don't need to make a request for all.
      .mergeMap((prodn2N1: string) => { // To forward both the Prodn2, which we will store in our field, and the Prodn1, which we will use to filter.
        const prodn1$ = this.http.get('/api/prodn1?producentid=' + prodn2N1);
        const prodn2N1$ = Observable.of(prodn2N1);
        return Observable.forkJoin(prodn1$, prodn2N1$);
      })
      .filter(response => { // Only forward those where we didn't find any Prodn1
        const noHit = response[0].json()[0] === null;
        return noHit;
      })
      .map(response => response[1]) // Pass prodn2N1 string
      .toArray()
      .subscribe(value => this.orphanProdn2s = value);
  }

  private updateProdn2sForDisplay() {
    this.prodn2sForDisplay = this.allProdn2s
      .filter((prodn2: Prodn2) => {
        if (this.selectedProdn1s.length > 0) {
          return this.selectedProdn1s.indexOf(prodn2.n1) > -1;
        } else {
          return true;
        }
      });
  }

  isSelected(prodn1: Prodn1) {
    return this.selectedProdn1s.indexOf(prodn1.producentid) > -1;
  }

  toggle(prodn1: Prodn1) {
    const indexOf = this.selectedProdn1s.indexOf(prodn1.producentid);
    if (indexOf > -1) {
      this.selectedProdn1s.splice(indexOf, 1);
    } else {
      this.selectedProdn1s.push(prodn1.producentid);
    }

    this.updateProdn2sForDisplay();
  }
}
