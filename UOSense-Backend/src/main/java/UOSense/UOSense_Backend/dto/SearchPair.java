package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchPair implements Comparable<SearchPair> {
    int distance;
    Restaurant restaurant;

    @Override
    public int compareTo(SearchPair other) {
        return Integer.compare(this.distance, other.distance);
    }
}
