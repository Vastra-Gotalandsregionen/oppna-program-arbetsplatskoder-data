package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by clalu4 on 2017-03-24.
 */
public abstract class AbstractEntity implements Serializable {


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractEntity) {
            if (((AbstractEntity) obj).getId() == this.getId()) {
                return true;
            }
            if (((AbstractEntity) obj).getId() == null || this.getId() == null) {
                return false;
            }
            return ((AbstractEntity) obj).getId().equals(this.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + getId() + "]";
    }

    public abstract <T extends Object> T getId();

}
