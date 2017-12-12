import {Component, Input, OnInit} from '@angular/core';
import {Data} from '../../model/data';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {RequestOptions, Headers, Response} from '@angular/http';
import {Ao3} from '../../model/ao3';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import {Vardform} from '../../model/vardform';
import {Verksamhet} from '../../model/verksamhet';
import {MatSnackBar} from '@angular/material';
import {StateService} from '../../core/state/state.service';
import {ErrorHandler} from '../../shared/error-handler';
import {Prodn1} from '../../model/prodn1';
import {Prodn2} from '../../model/prodn2';
import {Prodn3} from '../../model/prodn3';
import {JwtHttp} from '../../core/jwt-http';
import {RestResponse} from '../../model/rest-response';
import {ApkBase} from "../apk-base/apk-base";
import {Util} from "../../core/util/util";
import {UnitSearchResult} from "../../model/UnitSearchResult";

@Component({
  selector: 'apk-form',
  templateUrl: './apk-form.component.html',
  styleUrls: ['./apk-form.component.scss'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1, height: 'auto'})),
      state('out', style({opacity: 0, height: '0', margin: 0, padding: 0})),
      transition('* => *', animate('.2s ease'))
    ])
  ]
})
export class ApkFormComponent extends ApkBase implements OnInit {

  @Input('dataId') dataId: string;

  stateService: StateService;

  apkForm: FormGroup;

  data: Data;
  replacedBy: Data;
  $replaces: Observable<Data[]>;
  unitSearchResult: UnitSearchResult[];
  highestBeginningWith: string;

  hasArchivedDatas: boolean = false;

  allAo3s: Ao3[];
  allVardforms: Vardform[];
  allVerksamhets: Verksamhet[];
  allProdn1s: Prodn1[];

  filteredAo3Options: Observable<Ao3[]>;
  filteredVardformOptions: Observable<Vardform[]>;
  filteredVerksamhetOptions: Observable<Verksamhet[]>;

  prodn2Options: Prodn2[];
  prodn3Options: Prodn3[];

  ao3IdMap: Map<string, Ao3>;
  vardformIdMap: Map<string, Vardform>;
  verksamhetIdMap: Map<string, Verksamhet>;

  isPrivate: boolean;

  saveMessage: string;
  benamningKortActivelyEdited: boolean;

  messages: Map<string, string>;

  constructor(private http: JwtHttp,
              private formBuilder: FormBuilder,
              private snackBar: MatSnackBar,
              stateService: StateService,
              private errorHandler: ErrorHandler) {

    super();
    this.stateService = stateService;
  }

  ngOnInit() {

    const defaultData = new Data();

    // Messages:
    this.messages = new Map<string, string>();
    this.messages.set('field-is-required', 'Detta fält är obligatoriskt.');
    this.messages.set('field-is-invalid', 'Ogiltigt värde');
    this.messages.set('field-is-invalid-choose-from-dropdown-list', 'Ogiltigt värde. Du måste välja värde från dropdownlistan.');
    this.messages.set('code-already-exists', 'Koden finns redan. Välj en annan.');
    this.messages.set('invalid-beginning', 'Koden måste börja med 14.');
    this.messages.set('till-datum-is-before-from-datum', 'Giltig t.o.m. kan inte vara före Giltig fr.o.m.')

    // Default values:
    defaultData.externfakturamodell = 'nej';

    let dataObservable: Observable<Data>;
    if (this.dataId) {
      dataObservable = this.http.get('/api/data/' + this.dataId).map(response => response.json());
    } else {
      dataObservable = Observable.from([defaultData]);
    }

    const ao3Observable = this.http.get('/api/ao3').map(response => response.json().content);
    const vardformObservable = this.http.get('/api/vardform').map(response => response.json().content);
    const verksamhetObservable = this.http.get('/api/verksamhet').map(response => response.json());
    const prodn1 = this.http.get('/api/prodn1').map(response => response.json());

    Observable.forkJoin([ao3Observable, vardformObservable, verksamhetObservable, dataObservable, prodn1])
      .subscribe((result: any[]) => {
        const ao3s = result[0];
        const vardforms = result[1];
        const verksamhets = result[2];

        const data = <Data> result[3];

        let prodn1s = <Prodn1[]> result[4]; // These won't change as opposed to prodn2 and prodn3 which may change.

        prodn1s = prodn1s.filter(prodn1 => {
          return !prodn1.raderad || (data && data.prodn1 && data.prodn1.id === prodn1.id); // Don't show deleted ones if it isn't the one already saved for the data.
        });

        this.allAo3s = ao3s.filter((ao3: Ao3) => !ao3.raderad || ao3.ao3id === data.ao3);// Don't show deleted ones if it isn't the one already saved for the data.
        this.allVardforms = vardforms.filter((vardform: Vardform) => !vardform.raderad || (data.vardform == vardform.vardformid));
        this.allVerksamhets = verksamhets.filter((verksamhet: Verksamhet) => !verksamhet.raderad || data.verksamhet == verksamhet.verksamhetid); // Only show those not raderad.;

        this.data = data;

        this.allProdn1s = prodn1s;

        this.isPrivate = this.data.agarform === '4' || this.data.agarform === '5' || this.data.agarform === '6';

        let tempMap: Map<string, any> = new Map();
        for (const ao3 of ao3s) {
          tempMap.set(ao3.ao3id, ao3);
        }
        this.ao3IdMap = tempMap;

        tempMap = new Map();
        for (const vardform of vardforms) {
          tempMap.set(vardform.vardformid, vardform);
        }
        this.vardformIdMap = tempMap;

        tempMap = new Map();
        for (const verksamhet of verksamhets) {
          tempMap.set(verksamhet.verksamhetid, verksamhet);
        }
        this.verksamhetIdMap = tempMap;

        this.buildForm();

      });
  }

  getShowDebug(): boolean {
    return this.stateService.showDebug;
  }

  setShowDebug(value: boolean) {
    this.stateService.showDebug = value;
  }

  fromDateValidator(fromDateField): any {
    if (!this.apkForm) return null;
    var toDateField = this.apkForm.get('tillDatum');
    toDateField.updateValueAndValidity();
    return null;
  }

  toDateValidator(toDateField): any {
    if (!this.apkForm) return null;
    var fromDateField = this.apkForm.get('fromDatum');

    if (!toDateField.value) return null;
    if (!fromDateField.value) return null;
    if (toDateField.value < fromDateField.value) {
      return {toDateBeforeFromDate: toDateField.value};
    }

    return null;
  }

  private buildForm() {
    this.apkForm = this.formBuilder.group({
      'unitSearch': [],
      'arbetsplatskodlan': [{
        value: this.data.arbetsplatskodlan,
        disabled: true // Always start disabled since generateAutomatically starts as true
      }, [Validators.required], this.validateArbetsplatskodlan.bind(this)],
      'agarform': [this.data.agarform],
      'ao3': [this.ao3IdMap.get(this.data.ao3), Validators.required],
      'frivilligUppgift': [{value: this.data.frivilligUppgift, disabled: !this.isPrivate}],
      'ansvar': [this.data.ansvar, Validators.compose([ansvarValidator(), Validators.required])],
      'vardform': [this.vardformIdMap.get(this.data.vardform), Validators.required],
      'verksamhet': [this.verksamhetIdMap.get(this.data.verksamhet), Validators.required],
      'prodn1': [null, Validators.required], // Set further down
      'prodn2': [null, Validators.required], // Set further down
      'prodn3': [null, Validators.required], // Set further down
      'benamning': [this.data.benamning, Validators.required],
      'benamningKort': [this.data.benamningKort, Validators.required],
      'addressGroup': this.formBuilder.group({
        'postadress': [this.data.postadress, Validators.required],
        'postnr': [this.data.postnr, Validators.required],
        'postort': [this.data.postort, Validators.required],
      }),
      'externfakturaGroup': this.formBuilder.group({
        'externfakturamodell': [this.data.externfakturamodell, Validators.required]
      }),
      'externfaktura': [this.data.externfaktura, Validators.required],
      'groupCode': [this.data.groupCode],
      'vgpvGroup': this.formBuilder.group({
        'vgpv': [this.data.vgpv ? 'true' : 'false', Validators.required]
      }),
      'anmarkning': [this.data.anmarkning],
      'generateAutomatically': [true],
      'hsaid': [this.data.hsaid, hsaIdValidator()],

      'fromDatum': [
        {
          value: Util.dateStringToObject(this.data.fromDatum),
          disabled: false
        },
        Validators.compose([datePattern(), Validators.required, this.fromDateValidator.bind(this)])
      ],

      'noTillDatum': [!this.data.tillDatum || this.data.tillDatum.length === 0],
      'tillDatum': [
        {
          value: Util.dateStringToObject(this.data.tillDatum),
          disabled: !this.data.tillDatum || this.data.tillDatum.length == 0
        },
        Validators.compose([datePattern(), Validators.required, this.toDateValidator.bind(this)])
      ],
      //}, Validators.compose([datePattern(), Validators.required])],
      'ersattav': [this.data.ersattav, []],
    });

    this.apkForm.get('unitSearch').valueChanges
      .filter(value => {
        const requiredLength = value && value.length >= 3;
        if (!requiredLength) {
          this.unitSearchResult = [];
        }

        return requiredLength;
      })
      .flatMap(query => {
        return this.http.get('/api/search/unit?query=' + encodeURIComponent(query));
      })
      .retry() // So it retries even after an error.
      .map(result => result.json())
      .subscribe(
        result => this.unitSearchResult = result,
        error => console.log('error: ' + error)
      );


    // Init ersattav
    this.apkForm.get('ersattav').valueChanges
      .startWith(this.data.ersattav)
      .filter(value => {
        const isRequiredLength = value && value.length >= 3;

        if (!isRequiredLength) {
          this.replacedBy = null;
        }

        return isRequiredLength;
      })
      .flatMap(query => {
        return this.http.get('/api/data/arbetsplatskodlan/' + query);
      })
      .map(response => {
        try {
          return response.json();
        } catch (e) {
          return null;
        }
      })
      .subscribe((result: Data) => {
          this.replacedBy = result;
        }
      );

    this.$replaces = this.http.get('/api/data/ersattav/' + this.data.arbetsplatskodlan)
      .map(response => response.json());

    this.apkForm.statusChanges.subscribe(value => {
      if (value === 'VALID') {
        this.saveMessage = '';
      }
    });

    this.apkForm.get('noTillDatum').valueChanges
      .subscribe((checked: boolean) => {
        if (checked) {
          this.apkForm.get('tillDatum').disable();
        } else {
          this.apkForm.get('tillDatum').enable();
        }
      });

    this.apkForm.get('groupCode').valueChanges
      .subscribe((checked: boolean) => {
        if (checked) {
          this.apkForm.get('addressGroup').disable();
        } else {
          this.apkForm.get('addressGroup').enable();
        }
      });

    this.apkForm.get('agarform').valueChanges
      .subscribe((value: string) => {
        this.isPrivate = value === '4' || value === '5' || value === '6';
        // this.isPrivate = this.apkForm.value.agarform === '4' || this.apkForm.value.agarform === '5' || this.apkForm.value.agarform === '6';
        if (this.isPrivate) {
          this.apkForm.get('frivilligUppgift').enable();
        } else {
          this.apkForm.get('frivilligUppgift').disable();
        }
      });

    const benamning = this.apkForm.get('benamning');
    const benamningKort = this.apkForm.get('benamningKort');

    this.benamningKortActivelyEdited = benamningKort.value && benamningKort.value.length > 0;

    benamningKort.valueChanges.subscribe(value => {
      if (!value || value.length === 0) {
        this.benamningKortActivelyEdited = false;
      }
    });

    benamning.valueChanges
      .subscribe((value: string) => {
        if (!this.benamningKortActivelyEdited || benamningKort.value.length === 0) {
          if (value && value.length <= 35) {
            benamningKort.setValue(value);
          } else {
            benamningKort.setValue('');
          }
        }
      });

    this.initProdnControls(this.data.prodn3);

    if (this.data.id) {
      this.http.getPage('/api/archivedData/' + this.data.id)
        .map(response => response.json())
        .subscribe((archivedDatas: Data[]) => {
          this.hasArchivedDatas = archivedDatas.length > 0;
        });
    }

    this.initAo3FormControl();
    this.initExternFakturaModellControl();
    this.initVardformControl();
    this.initVerksamhetControl();
    this.initGenerateAutomaticallyControls();
  }

  private initVerksamhetControl() {
    const verksamhetFormControl = this.apkForm.get('verksamhet');

    this.filteredVerksamhetOptions = verksamhetFormControl.valueChanges
      .startWith(null)
      .map((name: string) => name ? this.filterVerksamhet(name) : this.allVerksamhets.slice());

    const map: Map<string, Verksamhet> = new Map();
    for (const verksamhet of this.allVerksamhets) {
      map.set(this.displayVerksamhetFn(verksamhet), verksamhet);
    }

    verksamhetFormControl.valueChanges.subscribe(value => {
      if (typeof value === 'string') {
        if (map.has(value)) {
          verksamhetFormControl.setValue(map.get(value));
        }
      }
    });

    verksamhetFormControl.setValidators([Validators.required, verksamhetValidator(this.allVerksamhets)]);
  }

  private initGenerateAutomaticallyControls() {
    const generateAutomaticallyFormControl = this.apkForm.get('generateAutomatically');
    const arbetsplatskodlanControl = this.apkForm.get('arbetsplatskodlan');
    const agarformControl = this.apkForm.get('agarform');

    generateAutomaticallyFormControl.valueChanges.subscribe(value => {
      if (value) {
        arbetsplatskodlanControl.disable();
      } else {
        arbetsplatskodlanControl.enable();
      }
    });

    arbetsplatskodlanControl.valueChanges
      .filter(value => value && value.length > 0)
      .flatMap(value => this.http.get('/api/data/highestBeginningWith/' + value))
      .map(response => response.json())
      .subscribe(datas => {
        arbetsplatskodlanControl.markAsTouched();
        if (datas.length > 0) {
              this.highestBeginningWith = datas[0].arbetsplatskodlan
            } else {
              this.highestBeginningWith = null;
            }
          }
        );

    agarformControl.valueChanges.subscribe(value => {
      if (value === '1' || value === '2' || value === '3' || value === '7' || value === '9') {
        generateAutomaticallyFormControl.setValue(true);
      } else if (value === '4' || value === '5' || value === '6') {
        generateAutomaticallyFormControl.setValue(false);
      }
    });
  }

  private initVardformControl() {
    const vardformFormControl = this.apkForm.get('vardform');

    this.filteredVardformOptions = vardformFormControl.valueChanges
      .startWith(null)
      .map((name: string) => name ? this.filterVardform(name) : this.allVardforms.slice());

    const map: Map<string, Vardform> = new Map();
    for (const vardform of this.allVardforms) {
      map.set(this.displayVardformFn(vardform), vardform);
    }

    vardformFormControl.valueChanges.subscribe(value => {
      if (typeof value === 'string') {
        if (map.has(value)) {
          vardformFormControl.setValue(map.get(value));
        }
      }
    });

    vardformFormControl.setValidators([Validators.required, vardformValidator(this.allVardforms)]);
  }

  private initAo3FormControl() {
    const ao3FormControl = this.apkForm.get('ao3');

    this.filteredAo3Options = ao3FormControl.valueChanges // Side effect
      .startWith(null)
      .map((name: string) => name ? this.filterAo3(name) : this.allAo3s.slice());

    const map: Map<string, Ao3> = new Map();
    for (const ao3 of this.allAo3s) {
      map.set(this.displayAo3Fn(ao3), ao3);
    }

    ao3FormControl.valueChanges.subscribe(value => {
      if (typeof value === 'string') {
        if (map.has(value)) {
          ao3FormControl.setValue(map.get(value));
        }
      }
    });

    ao3FormControl.setValidators([Validators.required, ao3Validator(this.allAo3s)])
  }

  private initExternFakturaModellControl() {
    const externFakturaModellControl = this.apkForm.get('externfakturaGroup').get('externfakturamodell');
    const externFakturControl = this.apkForm.get('externfaktura');

    externFakturaModellControl.valueChanges
      .startWith(externFakturaModellControl.value)
      .subscribe(value => {
        if (value === 'ovr') {
          externFakturControl.enable();
        } else {
          externFakturControl.disable();
        }
      });

  }

  private initProdnControls(prodn3: Prodn3) {
    if (prodn3) {
      // We assume the form is already built, so we don't need to fetch the prodn3 again.

      // Now we know prodn3. Based on that we want to find prodn2, prodn1 as well as options for prodn2 and prodn3.
      // Start by finding the specific prodn2
      this.http.get('/api/prodn3?prodn2=' + prodn3.prodn2.id)
        .map(response => response.json().content.filter(prodn3 => !prodn3.raderad || (this.data.prodn3.id === prodn3.id))) // Filter out deleted ones but keep it if it's already saved on the data.
        .do((prodn3s: Prodn3[]) => this.prodn3Options = prodn3s)
        .concatMap((prodn3s: Prodn3[]) => this.http.get('/api/prodn2?prodn1=' + prodn3.prodn2.prodn1.id)) // Moving on with finding the options for prodn2, based on prodn1
        .map(response => response.json().content)
        .subscribe((prodn2s: Prodn2[]) => {
          this.prodn2Options = prodn2s.filter(prodn2 => !prodn2.raderad || prodn3.prodn2.id === prodn2.id);

          // Need to get the exact instance to make the component find a match and be able to display an option from the option list.
          this.apkForm.patchValue({'prodn2': this.getItemInstanceInArray(prodn3.prodn2, this.prodn2Options)});
          this.apkForm.patchValue({'prodn1': this.getItemInstanceInArray(prodn3.prodn2.prodn1, this.allProdn1s)});
          this.apkForm.patchValue({'prodn3': this.getItemInstanceInArray(prodn3, this.prodn3Options)});

          this.listenToChangesToProdnx();
        });
    } else {
      this.listenToChangesToProdnx();
    }

  }

  private getItemInstanceInArray(item: any, array: any[]) {
    let filtered = array.filter(arrayItem => arrayItem.id === item.id);

    if (filtered.length > 0) {
      return filtered[0];
    } else {
      throw new Error('Entity not found.');
    }
  }

  listenToChangesToProdnx() {
    const prodn1Control = this.apkForm.get('prodn1');
    const prodn2Control = this.apkForm.get('prodn2');

    prodn1Control.valueChanges
      .filter(value => value ? true : false)
      .flatMap(prodn1 =>
        this.http.get('/api/prodn2?prodn1=' + prodn1.id).map(response => response.json().content))
      .subscribe(prodn2s => {
        this.prodn2Options = prodn2s.filter(prodn2 => !prodn2.raderad); // Only show those not raderad.
        this.prodn3Options = [];
        this.apkForm.patchValue({
          'prodn2': null,
          'prodn3': null
        });
      });

    prodn2Control.valueChanges
      .filter(value => value ? true : false)
      .flatMap(prodn2 => this.http.get('/api/prodn3?prodn2=' + prodn2.id))
      .map(response => response.json())
      .map((pageable: RestResponse<Prodn3[]>) => <Prodn3[]>pageable.content)
      .subscribe(prodn3s => {
        this.prodn3Options = prodn3s.filter(prodn3 => !prodn3.raderad); // Only show those not raderad.
        this.apkForm.patchValue({
          'prodn3': null
        });
      });
  }

  save() {
    if (!this.apkForm.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const data = this.clone(this.data);
    const formModel = this.apkForm.value;
    data.lankod = data.lankod || '14'; // Hard-coded
    data.arbetsplatskodlan = data.arbetsplatskodlan || formModel.arbetsplatskodlan;
    data.agarform = formModel.agarform;
    data.groupCode = formModel.groupCode;
    data.ao3 = (<Ao3> formModel.ao3).ao3id;
    data.anmarkning = formModel.anmarkning;
    data.ansvar = formModel.ansvar;
    data.frivilligUppgift = formModel.frivilligUppgift;
    data.hsaid = formModel.hsaid.trim();
    data.vardform = (<Vardform> formModel.vardform).vardformid;
    data.verksamhet = (<Verksamhet> formModel.verksamhet).verksamhetid;
    data.benamning = formModel.benamning;
    data.benamningKort = formModel.benamningKort;
    if (!formModel.groupCode) {
      data.postadress = formModel.addressGroup.postadress;
      data.postnr = formModel.addressGroup.postnr;
      data.postort = formModel.addressGroup.postort;
    }
    data.externfakturamodell = formModel.externfakturaGroup.externfakturamodell;
    data.externfaktura = formModel.externfaktura;
    data.prodn1 = formModel.prodn1;
    data.prodn3 = formModel.prodn3;
    data.vgpv = formModel.vgpvGroup.vgpv;
    data.fromDatum = formModel.fromDatum && typeof formModel.fromDatum === 'object' ? Util.dateToString(formModel.fromDatum) : formModel.fromDatum;
    data.tillDatum = formModel.tillDatum && typeof formModel.tillDatum === 'object' ? Util.dateToString(formModel.tillDatum) : formModel.tillDatum;
    data.ersattav = formModel.ersattav;

    const headers = new Headers({'Content-Type': 'application/json'});
    const options = new RequestOptions({headers: headers});

    this.http.put('/api/data', JSON.stringify(data), options)
      .map(response => response.json())
      .subscribe(
        (data: Data) => {
          this.data = data;
          this.buildForm();
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
        },
        error => {
          console.log('Error: ' + error);
        });
  }

  private clone(source: Data): Data {
    let target = new Data();
    Object.assign(target, source);
    return target;
  }

  resetForm() {
    this.apkForm.reset();
    this.buildForm();
  }

  filterAo3(name: string): Ao3[] {
    return this.allAo3s.filter(option =>
      new RegExp(name, 'gi').test(option.foretagsnamn) ||
      new RegExp(name, 'gi').test(option.ao3id) ||
      new RegExp(name, 'gi').test(this.displayAo3Fn(option)));
  }

  filterVardform(name: string): Vardform[] {
    return this.allVardforms.filter(option =>
      new RegExp(name, 'gi').test(option.vardformtext) ||
      new RegExp(name, 'gi').test(option.vardformid) ||
      new RegExp(name, 'gi').test(this.displayVardformFn(option)));
  }

  filterVerksamhet(name: string): Verksamhet[] {
    return this.allVerksamhets.filter(option =>
      new RegExp(name, 'gi').test(option.verksamhettext) ||
      new RegExp(name, 'gi').test(option.verksamhetid) ||
      new RegExp(name, 'gi').test(this.displayVerksamhetFn(option)));
  }

  displayAo3Fn(ao3: Ao3): string {
    return ao3 ? ao3.ao3id + ' - ' + ao3.foretagsnamn : '';
  }

  displayVardformFn(vardform: Vardform): string {
    return vardform ? vardform.vardformid + ' - ' + vardform.vardformtext : '';
  }

  displayVerksamhetFn(verksamhet: Verksamhet): string {
    return verksamhet ? verksamhet.verksamhetid + ' - ' + verksamhet.verksamhettext : '';
  }

  displayUnitFn(unit: any): string {
    return unit && unit.attributes && unit.attributes.ou && unit.attributes.ou.length > 0 ? unit.attributes.ou[0] : '';
  }

  getAddressObject(unit: any) {
    if (!(unit && unit.attributes)) {
      return {};
    }

    let parts;
    if (unit.attributes.hsaStreetAddress && unit.attributes.hsaStreetAddress.length > 0) {
      parts = unit.attributes.hsaStreetAddress[0].split('$');
    } else {
      parts = ['', '', ''];
    }

    return {
      'postadress': parts[0],
      'postnr': parts[1],
      'postort': parts[2]
    }
  }

  firstValue(array: any[]) {
    if (array && array.length > 0) {
      return array[0];
    }

    return null;
  }

  selectUnit(unit: any): void {
    this.apkForm.patchValue({
      'addressGroup': this.getAddressObject(unit)
    });

    this.apkForm.patchValue({
      'ao3': unit.attributes.vgrAo3kod ? this.ao3IdMap.get(unit.attributes.vgrAo3kod[0]) : null,
      'hsaid': unit.attributes.hsaIdentity ? unit.attributes.hsaIdentity[0] : null,
      'benamning': unit.attributes.ou ? unit.attributes.ou[0] : null,
    });

    this.apkForm.get('ao3').markAsTouched();
    this.apkForm.get('hsaid').markAsTouched();
    this.apkForm.get('benamning').markAsTouched();
    this.apkForm.get('addressGroup').markAsTouched();
  }

  dnToFriendly(dnString: string) {
    if (!dnString || dnString.length === 0) {
      return '';
    }
    // E.g. ou=Verksamhet Neonatologi,ou=Område 1,ou=Sahlgrenska Universitetssjukhuset,ou=Org,o=VGR

    const parts = dnString.split(',');

    const allPartsExceptLastTwo = parts.slice(0, parts.length - 2);

    const readFriendlyParts = allPartsExceptLastTwo.map(part => part.split('=')[1]);

    const inOppositeOrder = readFriendlyParts.reverse();

    return inOppositeOrder.join(' > ');
  }

  benamningKortKeyDown($event: any) {
    if ($event.key.length === 1) {
      // Otherwise it's not a key which writes anything.
      this.benamningKortActivelyEdited = true;
    }
  }

  // Convenience method for less code in html file.
  getErrors(formControlName: string): any {
    return this.apkForm.controls[formControlName].errors;
  }

  validateArbetsplatskodlan(control: AbstractControl): Observable<{ [key: string]: any }> {
    if (control.value) {
      if (control.value.length >= 2 && !control.value.startsWith('14')) {
        console.log('fel början');
        return new Observable(observer => observer.next({invalidBeginning: control.value})).take(1);
      }
      return this.http.get('/api/data/arbetsplatskodlan/' + control.value)
        .map(response => {
          try {
            return response.json();
          } catch (e) {
            return null;
          }
        })
        .map(data => {
          if (data) {
            return {alreadyExists: control.value};
          } else {
            return null;
          }
        });
    } else {
      return new Observable(observer => observer.next(null)).take(1);
    }
  };

}

export function ao3Validator(ao3s: Ao3[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    let value = control.value;
    return value && value.length > 0 && ao3s.indexOf(value) === -1 ? {'invalidName': value} : null;
  };
}

export function ansvarValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    let value = <string>control.value;
    return value && (value.match('^[0-9]+$') && value.length >= 4 && value.length <= 6) ? null : {'invalidName': value};
    // return  /^[0-9]+$/.test(val);
  };
}

export function hsaIdValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    let value = <string>control.value;

    if (!value || value.length === 0 || value.toLowerCase() === 'saknas') {
      return null;
    }

    if (value.length !== 26) {
      return {'invalidName': value};
    }

    if (!value.startsWith('SE')) {
      return {'invalidName': value};
    }

    if (!(value.charAt(13) === 'E' || value.charAt(13) === 'F')) {
      return {'invalidName': value};
    }

    return null;
  };
}

export function vardformValidator(vardforms: Vardform[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    let value = control.value;
    return value && value.length > 0 && vardforms.indexOf(value) === -1 ? {'invalidName': value} : null;
  };
}

export function verksamhetValidator(verksamhets: Verksamhet[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    return verksamhets.indexOf(control.value) === -1 ? {'invalidName': control.value} : null;
  };
}

export function datePattern(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    const datePattern = /^\d{4}-\d{2}-\d{2}$/;

    let value = control.value;

    if (value && typeof value === 'object') {
      value = Util.dateToString(value);
    }

    if (!value || !value.match(datePattern)) {
      return {'datePattern': true};
    }

    return null;
  }
}
