package techcourse.jcf.mission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomListTest {

    private CustomList customList;

    @BeforeEach
    void init() {
        customList = new CustomList();
        customList.add("hi");
        customList.add("wootecho");
        customList.add("mission");
    }

    @Test
    @DisplayName("원소를 삽입한 뒤, 성공적으로 삽입이 완료되면 true를 리턴한다.")
    void add_test() {
        assertThat(customList.add("journey"))
                .isTrue();
    }

    @Test
    @DisplayName("유효한 범위의 위치에 있는 값을 조회하면, 해당 값이 출력된다.")
    void get_success_test() {
        assertThat(customList.get(0))
                .isEqualTo("hi");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    @DisplayName("유효한 범위의 위치가 아닌 값을 조회하면, 예외가 발생한다.")
    void get_fail_test(final int invalidIndex) {
        assertThatThrownBy(() -> customList.get(invalidIndex))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("유효한 범위의 위치에 있는 값을 수정하면, 해당 인덱스의 값이 변화한다.")
    void set_success_test() {
        String beforeValue = customList.get(0);

        assertThat(beforeValue)
                .isEqualTo("hi");

        assertThat(customList.set(0, "hihi"))
                .isEqualTo("hihi");

        String afterValue = customList.get(0);

        assertThat(afterValue)
                .isEqualTo("hihi");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    @DisplayName("유효한 범위의 위치가 아닌 값을 수정하면, 예외가 발생한다.")
    void set_fail_test(final int invalidIndex) {
        assertThatThrownBy(() -> customList.set(invalidIndex, "new"))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"hi:0", "wootecho:1", "mission:2"}, delimiter = ':')
    @DisplayName("찾고자 하는 값이 존재한다면, 해당 값의 인덱스를 반환한다.")
    void indexOf_success_test(final String targetValue, final int targetIndex) {
        assertThat(customList.indexOf(targetValue))
                .isEqualTo(targetIndex);
    }

    @ParameterizedTest
    @ValueSource(strings = {"hihi", " wootecho", "mission "})
    @DisplayName("찾고자 하는 값이 존재하지 않는다면, -1을 반환한다.")
    void indexOf_not_found_test(final String targetValue) {
        assertThat(customList.indexOf(targetValue))
                .isEqualTo(-1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"hi", "wootecho", "mission"})
    @DisplayName("찾고자 하는 값이 존재하면, true을 반환한다.")
    void contains_true_test(final String targetValue) {
        assertThat(customList.contains(targetValue))
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"hihi", " wootecho", "mission "})
    @DisplayName("찾고자 하는 값이 존재하지 않는다면, false를 반환한다.")
    void contains_false_test(final String targetValue) {
        assertThat(customList.contains(targetValue))
                .isFalse();
    }

    @Test
    @DisplayName("리스트에 존재하는 원소의 개수를 반환해야 한다.")
    void size_success_test() {
        assertThat(customList.size())
                .isEqualTo(3);
    }

    @Test
    @DisplayName("리스트의 원소를 비우면, 리스트의 사이즈는 0이 된다.")
    void clear_success_test() {
        customList.clear();
        assertThat(customList.size())
                .isEqualTo(0);
    }

    @Test
    @DisplayName("리스트에 존재하는 원소가 존재하지 않으면 true를 반환한다.")
    void isEmpty_true_test() {
        customList.clear();
        assertThat(customList.isEmpty())
                .isTrue();
    }

    @Test
    @DisplayName("리스트에 존재하는 원소가 존재하면 false를 반환한다.")
    void isEmpty_false_test() {
        assertThat(customList.isEmpty())
                .isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = {"hi:0", "wootecho:1", "mission:2"}, delimiter = ':')
    @DisplayName("범위 내의 원소를 제거하면, 제거한 원소를 반환한다.")
    void removeWithIndex_success_test(final String targetValue, final int targetIndex) {
        assertThat(customList.remove(targetIndex))
                .isEqualTo(targetValue);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    @DisplayName("범위 외의 원소를 제거하면, 예외가 발생한다.")
    void removeWithIndex_fail_test(final int invalidIndex) {
        assertThatThrownBy(() -> customList.remove(invalidIndex))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"hi", "wootecho", "mission"})
    @DisplayName("존재하는 원소를 제거하려고 하면, true를 반환한다.")
    void removeWithValue_true_test(final String targetValue) {
        assertThat(customList.remove(targetValue))
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"hihi", " wootecho", "mission "})
    @DisplayName("존재하지 않는 원소를 제거하려고 하면, false를 반환한다.")
    void removeWithValue_false_test(final String targetValue) {
        assertThat(customList.remove(targetValue))
                .isFalse();
    }
}