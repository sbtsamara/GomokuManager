using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSBot
{
    public class Path
    {
        public enum Direction
        {
            Horizontal,
            Vertical,
            DiagonalUp,
            DiagonalDown
        }

        public Vector start { get; private set; }
        public Direction direct { get; private set; }
        public int length { get; set; }

        public Path(Vector start, Direction direct, int length = 1)
        {
            this.start = start;
            this.direct = direct;
            this.length = length;
        }
    }
}
